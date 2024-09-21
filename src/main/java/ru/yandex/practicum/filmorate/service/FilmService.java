package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;
    private final RatingService ratingService;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(long id) {
        return filmStorage.getById(id);
    }

    public Film create(Film film) {
        ratingService.validateMpaId(film.getMpa().getId());
        genreService.validateGenreId(film.getGenres());
        return filmStorage.create(film);
    }

    public Film update(Film filmNewInfo) {
        return filmStorage.update(filmNewInfo);
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

    public List<Film> getMostLikedFilms(int count) {
        if (count == 0) {
            count = 10;
        }
        return filmStorage.getPopular(count);
    }
}