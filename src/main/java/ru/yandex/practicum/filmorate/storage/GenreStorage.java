package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

public interface GenreStorage {

    Collection<Genre> getAll();

    Optional<Genre> getById(int id);

    int getCountOfGenres();

    void setFilmGenres(Film film);

    void saveGenresInfo(long filmId, List<Genre> genres);

    boolean containsFilmGenre(long genreId, long filmId);
}