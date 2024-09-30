package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.Optional;

public interface RatingStorage {

    Collection<Rating> getAll();

    Optional<Rating> getById(long id);

    int getCountOfMpa();

    void setFilmRating(Film film);
}