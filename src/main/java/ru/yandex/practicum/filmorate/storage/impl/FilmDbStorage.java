package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.*;

@Component
@AllArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    public final JdbcTemplate jdbcTemplate;
    public final GenreStorage genreStorage;
    public final RatingStorage ratingStorage;

    @Override
    public Film getById(long id) {
        Film film = jdbcTemplate.queryForObject("SELECT * FROM films WHERE film_id = ?",
                new FilmMapper(), id);
        return ratingStorage.setFilmRating(genreStorage.setFilmGenres(film));
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = """
                INSERT INTO films(name, description, release_date, duration, rating_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        String returnSqlQuery = """
        SELECT *
        FROM films
        WHERE name = ? AND release_date = ?
        ORDER BY film_id DESC
        LIMIT(1)""";

        Film filmFromDb = jdbcTemplate.queryForObject(returnSqlQuery,
                new FilmMapper(), film.getName(), film.getReleaseDate());

        genreStorage.saveGenresInfo(filmFromDb.getId(), film.getGenres());

        return getById(filmFromDb.getId());
    }

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM films", new FilmMapper());
    }

    @Override
    public Film update(Film filmNewInfo) {
        String sqlQuery = """
                UPDATE films SET
                name = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                rating_id = ?
                where film_id = ?
                """;

        int matches = jdbcTemplate.update(sqlQuery,
                filmNewInfo.getName(),
                filmNewInfo.getDescription(),
                filmNewInfo.getReleaseDate(),
                filmNewInfo.getDuration(),
                filmNewInfo.getMpa().getId(),
                filmNewInfo.getId());

        if (matches == 0) {
            throw new NullPointerException();
        }

        return filmNewInfo;
    }

    @Override
    public List<Film> getPopular(int count) {
        String query = """
                SELECT f.FILM_ID,
                	   f.name,
                       f.DESCRIPTION,
                       f.RELEASE_DATE,
                       f.DURATION,
                       r.RATING_ID,
                       r.rating_name,
                       COUNT(user_id) AS likes
                FROM USER_LIKED_FILMS ulf
                JOIN FILMS f ON ulf.FILM_ID = f.FILM_ID
                JOIN RATING r ON f.RATING_ID = r.RATING_ID
                GROUP BY ulf.FILM_ID
                ORDER BY likes DESC
                LIMIT (?)""";

        return jdbcTemplate.query(query, new FilmMapper(), count);
    }

    @Override
    public void addLike(long userId, long filmId) {
        jdbcTemplate.update("INSERT INTO user_liked_films VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        jdbcTemplate.update("DELETE FROM user_liked_films WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    @Override
    public boolean containsLike(long userId, long filmId) {
        return !jdbcTemplate.queryForList("SELECT * FROM user_liked_films WHERE user_id = ? AND film_id = ?",
                userId, filmId).isEmpty();
    }
}