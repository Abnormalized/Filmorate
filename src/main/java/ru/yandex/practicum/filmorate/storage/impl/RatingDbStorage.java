package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
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
}