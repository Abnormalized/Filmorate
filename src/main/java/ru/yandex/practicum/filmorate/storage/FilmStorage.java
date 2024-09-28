package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film getById(long id);

    Film create(@RequestBody Film film);

    Collection<Film> findAll();

    Film update(@RequestBody Film filmNewInfo);

    List<Film> getPopular(int count);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    boolean containsLike(long userId, long filmId);

    Collection<Film> getDirectorFilms(long directorId, String sortType);

    void deleteFilmById(long id);

    boolean checkById(long id);
}