package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    Film create(@RequestBody Film film);

    long newId();

    Collection<Film> findAll();

    Film update(@RequestBody Film filmNewInfo);
}