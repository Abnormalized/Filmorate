package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(long userId, long filmId) {
        Film film = filmStorage.films.get(filmId);
        User user = userStorage.users.get(userId);
        if (user.getLikedFilms().contains(filmId)) {
            throw new NullPointerException();
        }
        user.getLikedFilms().add(filmId);
        film.setCountOfLikes(film.getCountOfLikes() + 1);
    }

    public void removeLike(long userId, long filmId) {
        Film film = filmStorage.films.get(filmId);
        User user = userStorage.users.get(userId);
        if (!user.getLikedFilms().contains(filmId)) {
            throw new NullPointerException();
        }
        user.getLikedFilms().remove(filmId);
        film.setCountOfLikes(film.getCountOfLikes() - 1);
    }

    public List<Film> getMostLikedFilms(int count) {
        if (count == 0) {
            count = 10;
        }
        return filmStorage.findAll().stream()
                .sorted((film1, film2) -> Long.compare(film2.getCountOfLikes(), film1.getCountOfLikes()))
                .limit(count)
                .toList();
    }
}