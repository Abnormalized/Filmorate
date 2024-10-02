package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {

    final FilmStorage filmStorage;
    final GenreService genreService;
    final RatingService ratingService;
    final DirectorService directorService;
    final FeedService feedService;
    final ValidationService validationService;

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.findAll();
        loadGenres(films);
        loadDirectors(films);
        return films;
    }

    public Collection<Film> findAll(String searchQuery, String by) {
        Collection<Film> films = filmStorage.findAll(searchQuery, by);
        loadGenres(films);
        loadDirectors(films);
        loadDirectors(films);
        return films;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм с id " + id + " не найден"));
        directorService.setFilmDirectors(film);
        return film;
    }

    public Film create(Film film) {
        ratingService.validateMpaId(film.getMpa().getId());
        genreService.validateGenreId(film.getGenres());
        directorService.validateDirectorsId(film.getDirectors());
        Film createdFilm = filmStorage.create(film);
        directorService.setFilmDirectors(createdFilm);
        return createdFilm;
    }

    public Film update(Film filmNewInfo) {
        validationService.validateFilmPresenceById(filmNewInfo.getId());
        Film film = filmStorage.update(filmNewInfo);
        directorService.setFilmDirectors(film);
        return film;
    }

    public void addLike(long userId, long filmId) {
        validationService.validateFilmPresenceById(filmId);
        validationService.validateUserPresenceById(userId);
        if (!filmStorage.containsLike(userId, filmId)) {
            filmStorage.addLike(userId, filmId);
        }
        feedService.addFeed(userId, EventType.LIKE, Operation.ADD, filmId);
    }

    public void removeLike(long userId, long filmId) {
        validationService.validateFilmPresenceById(filmId);
        validationService.validateUserPresenceById(userId);
        if (filmStorage.containsLike(userId, filmId)) {
            filmStorage.removeLike(userId, filmId);
            feedService.addFeed(userId, EventType.LIKE, Operation.REMOVE, filmId);
        }
    }

    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        Collection<Film> films = filmStorage.getPopularFilm(count, genreId, year);
        loadGenres(films);
        loadDirectors(films);
        return films;
    }

    public Collection<Film> getDirectorFilms(long directorId, String sortType) {

        directorService.getDirectorById(directorId);
        Collection<Film> films = filmStorage.getDirectorFilms(directorId, sortType);
        loadGenres(films);
        loadDirectors(films);
        return films;
    }

    public Collection<Film> getRecommendations(long userId) {

        Collection<Film> films = filmStorage.getRecommendations(userId);
        loadGenres(films);
        loadDirectors(films);

        return films;
    }

    public void loadGenres(Collection<Film> films) {
        genreService.loadGenres(films);
    }

    public void loadDirectors(Collection<Film> films) {
        directorService.loadDirectors(films);
    }

    public void deleteFilmById(long id) {
        validationService.validateFilmPresenceById(id);
        filmStorage.deleteFilmById(id);
    }
}