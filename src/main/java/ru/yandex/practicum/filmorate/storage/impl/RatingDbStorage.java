package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Rating> rowMapper;

    @Override
    public Collection<Rating> getAll() {
        return jdbcTemplate.query("SELECT * FROM rating", rowMapper);
    }

    @Override
    public Optional<Rating> getById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM rating WHERE rating_id = ?",
                    rowMapper, id));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public int getCountOfMpa() {
        Integer num = jdbcTemplate.queryForObject("SELECT COUNT(rating_id) FROM rating", Integer.class);
        return Objects.requireNonNullElse(num, 0);
    }

    @Override
    public void setFilmRating(Film film) {
        film.setMpa(jdbcTemplate.queryForObject("SELECT * FROM rating WHERE rating_id = ? LIMIT (1)",
                rowMapper, film.getMpa().getId()));
    }
}