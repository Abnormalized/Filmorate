package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.SQLException;
import java.util.Collection;
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
        userStorage.getById(id).orElseThrow(() -> new NoSuchElementException("Пользователь с id " + id + " не найден"));;
    }
}
