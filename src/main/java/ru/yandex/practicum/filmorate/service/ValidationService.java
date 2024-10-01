package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationService {

    final FilmStorage filmStorage;
    final UserStorage userStorage;

    public void validateFilmPresenceById(long id) {
        filmStorage.getById(id).orElseThrow(() -> new NoSuchElementException("Фильм с id " + id + " не найден"));
    }

    public void validateUserPresenceById(long id) {
        userStorage.getById(id).orElseThrow(() -> new NoSuchElementException("Пользователь с id " + id + " не найден"));
    }
}
