package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {

    final FilmStorage filmStorage;
    final GenreService genreService;
    final RatingService ratingService;
    final DirectorService directorService;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм с id " + id + " не найден"));
        setFilmProperties(film);
        return film;
    }

    public void validateFilmPresenceById(long id) {
        filmStorage.getById(id).orElseThrow(() -> new NoSuchElementException("Фильм с id " + id + " не найден"));
    }

    public Film create(Film film) {
        ratingService.validateMpaId(film.getMpa().getId());
        genreService.validateGenreId(film.getGenres());
        directorService.validateDirectorsId(film.getDirectors());
        Film createdFilm = filmStorage.create(film);
        setFilmProperties(createdFilm);
        return createdFilm;
    }

    public Film update(Film filmNewInfo) {
        Film film = filmStorage.update(filmNewInfo);
        setFilmProperties(film);
        return film;
    }

    public void addLike(long userId, long filmId) {
        if (!filmStorage.containsLike(userId, filmId)) {
            filmStorage.addLike(userId, filmId);
        }
    }

    public void removeLike(long userId, long filmId) {
        if (filmStorage.containsLike(userId, filmId)) {
            filmStorage.removeLike(userId, filmId);
        }
    }

    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        return filmStorage.getPopularFilm(count, genreId, year);
    }

    public Collection<Film> getDirectorFilms(long directorId, String sortType) {
        Collection<Film> films = filmStorage.getDirectorFilms(directorId, sortType);
        for (Film film : films) {
            setFilmProperties(film);
        }
        return films;
    }

    public void setFilmProperties(Film film) {
        ratingService.setFilmRating(film);
        genreService.setFilmGenres(film);
        directorService.setFilmDirectors(film);
    }
}