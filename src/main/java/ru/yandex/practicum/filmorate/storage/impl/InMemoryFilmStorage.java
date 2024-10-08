package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getById(long id) {
        return films.get(id);
    }

    public Film create(@RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        film.setId(newId());
        films.put(film.getId(), film);
        log.info("Создан фильм ID:{}, Name:{}", film.getId(), film.getName());
        return film;
    }

    public long newId() {
        log.trace("Запрос на генерацию нового ID");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Выдан ID:{}", (currentMaxId + 1));
        return ++currentMaxId;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film update(@RequestBody Film filmNewInfo) {
        log.debug("Запрос на обновление данных о фильме");
        if (!films.containsKey(filmNewInfo.getId())) {
            log.warn("Запрашиваемый фильм с ID: {} не найден", filmNewInfo.getId());
            throw new NullPointerException();
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

    @Override
    public List<Film> getPopular(int count) {
        return null;
    }

    @Override
    public void addLike(long userId, long filmId) {

    }

    @Override
    public void removeLike(long userId, long filmId) {

    }

    @Override
    public boolean containsLike(long userId, long filmId) {
        return false;
    }
}