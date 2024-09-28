package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> getById(long id);

    Film create(@RequestBody Film film);

    Collection<Film> findAll();

    Film update(@RequestBody Film filmNewInfo);

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    boolean containsLike(long userId, long filmId);

    Collection<Film> getDirectorFilms(long directorId, String sortType);

    void deleteFilmById(long id);

    boolean checkById(long id);
}