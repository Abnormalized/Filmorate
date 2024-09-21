package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.mapper.RatingMapper;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {

    public final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Rating> getAll() {
        return jdbcTemplate.query("SELECT * FROM rating", new RatingMapper());
    }

    @Override
    public Optional<Rating> getById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM rating WHERE rating_id = ?", new RatingMapper(), id)
            );
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public int getCountOfMpa() {
        return jdbcTemplate.queryForObject("SELECT COUNT(rating_id) FROM rating", Integer.class);
    }

    @Override
    public Film setFilmRating(Film film) {
        film.setMpa(jdbcTemplate.queryForObject("SELECT * FROM rating WHERE rating_id = ? LIMIT (1)",
                new RatingMapper(), film.getMpa().getId()));
        return film;
    }
}