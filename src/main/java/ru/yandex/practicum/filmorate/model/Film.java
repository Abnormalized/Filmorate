package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.validator.NotBeforeDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(of = {"id", "name"})
@Data
@Slf4j
public class Film {
    public static final Map<Long, Film> films = new HashMap<>();

    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotBeforeDate(value = "1895-12-28", message = "Дата не может быть раньше дня создания кинемотографии")
    private LocalDate releaseDate;
    @NotNull
    @DurationMin
    private Duration duration;

    public long getDuration() {
        return duration.toSeconds();
    }

    public static Collection<Film> findAll() {
        return films.values();
    }

    public static Film create(@RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        film.setId(newId());
        films.put(film.getId(), film);
        log.info("Создан фильм ID:{}, Name:{}", film.getId(), film.getName());
        return film;
    }

    public static Film update(@RequestBody Film filmNewInfo) {
        log.debug("Запрос на обновление данных о фильме");
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
        log.info("Обновлены данные о фильме ID:{}, Name:{}", film.getId(), film.getName());
        return film;
    }

    private static long newId() {
        log.trace("Запрос на генерацию нового ID");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Выдан ID:{}", (currentMaxId + 1));
        return ++currentMaxId;
    }
}