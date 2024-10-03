package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> getById(long id);

    Film create(Film film);

    Collection<Film> findAll();

    Collection<Film> findAll(String searchQuery, String by);

    Film update(Film filmNewInfo);

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    boolean containsLike(long userId, long filmId);

    Collection<Film> getDirectorFilms(long directorId, String sortType);

    Collection<Film> getRecommendations(long userId);

    void deleteFilmById(long id);

}