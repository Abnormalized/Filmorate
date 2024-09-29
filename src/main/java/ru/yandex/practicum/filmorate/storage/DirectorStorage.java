package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface DirectorStorage {

    Collection<Director> findAll();

    Optional<Director> getById(long id);

    Director create(Director director);

    Director update(Director directorNewInfo);

    void delete(long id);

    int getCountOfDirectors();

    void saveDirectorsInfo(long filmId, List<Director> directors);

    boolean containsDirectorFilm(long directorId, long filmId);

    void setFilmDirectors(Film film);

    void loadDirectors(Collection<Film> films);
}