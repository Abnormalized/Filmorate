package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(of = {"id", "name"})
@Data
@Slf4j
public class Film {
    public static final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FILM_INVENTION_DAY = LocalDate.of(1895, 12, 28);

    private long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Слишком длинное описание (max:200)")
    private String description;
    private LocalDate releaseDate;
    @NotNull
    private Duration duration;

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        if (releaseDate != null && releaseDate.isBefore(FILM_INVENTION_DAY)) {
            log.warn("Задана дата выхода фильма: {}", releaseDate);
            throw new ValidationException("Введены некорректные данные");
        } else if (duration != null && duration.isNegative()) {
            log.warn("Задана длительность фильма: {}", duration);
            throw new ValidationException("Введены некорректные данные");
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofSeconds(duration.getSeconds());
        setId(newId());
    }

    public long getDuration() {
        return duration.toSeconds();
    }

    public static Collection<Film> findAll() {
        log.info("Запрос на выдачу всех фильмов");
        return films.values();
    }

    public static Film create(@RequestBody Film film) {
        log.info("Запрос на создание фильма");
        films.put(film.getId(), film);
        log.debug("Фильм добавлен в список фильмов под ID:{}", film.getId());
        return film;
    }

    public static Film update(@RequestBody Film filmNewInfo) {
        log.info("Запрос на обновление данных о фильме");
        if (!films.containsKey(filmNewInfo.getId())) {
            log.warn("Запрашиваемый фильм с ID: {} не найден", filmNewInfo.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Film film = films.get(filmNewInfo.getId());
        film.setName(Objects.requireNonNullElse(filmNewInfo.getName(), film.getName()));
        film.setDescription(Objects.requireNonNullElse(filmNewInfo.getDescription(), film.getDescription()));
        film.setReleaseDate(Objects.requireNonNullElse(filmNewInfo.getReleaseDate(), film.getReleaseDate()));
        film.setDuration(Objects.requireNonNullElse(Duration.ofSeconds(filmNewInfo.getDuration()),
                Duration.ofSeconds(film.getDuration())));
        log.debug("Данные о фильме обновлены");
        return film;
    }

    private static long newId() {
        log.trace("Запрос на генерацию нового ID");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Выданный ID: {}", (currentMaxId + 1));
        return ++currentMaxId;
    }
}