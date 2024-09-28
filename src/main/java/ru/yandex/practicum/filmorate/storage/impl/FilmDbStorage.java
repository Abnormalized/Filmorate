package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@AllArgsConstructor
@Primary
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDbStorage implements FilmStorage {

    final JdbcTemplate jdbcTemplate;
    final GenreStorage genreStorage;
    final DirectorStorage directorStorage;

    @Override
    public Film getById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM films WHERE film_id = ?",
                new FilmMapper(), id);
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
        directorStorage.saveDirectorsInfo(filmFromDb.getId(), film.getDirectors());

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
            throw new NoSuchElementException();
        }

        String returnSqlQuery = """
                SELECT *
                FROM films
                WHERE name = ? AND release_date = ?
                ORDER BY film_id DESC
                LIMIT(1)""";

        Film filmFromDb = jdbcTemplate.queryForObject(returnSqlQuery,
                new FilmMapper(), filmNewInfo.getName(), filmNewInfo.getReleaseDate());

        genreStorage.saveGenresInfo(filmFromDb.getId(), filmNewInfo.getGenres());
        directorStorage.saveDirectorsInfo(filmFromDb.getId(), filmNewInfo.getDirectors());

        return getById(filmFromDb.getId());
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        String sql = "SELECT f.*, COUNT(l.film_id) AS like_count " +
                "FROM films f " +
                "LEFT JOIN user_liked_films l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genre fg ON f.film_id = fg.film_id " +
                "WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (genreId != null) {
            sql += " AND fg.genre_id = ?";
            params.add(genreId);
        }

        if (year != null) {
            sql += " AND YEAR(f.release_date) = ?";
            params.add(year);
        }

        sql += " GROUP BY f.name, f.film_id " +
                "ORDER BY COUNT(l.film_id) DESC LIMIT ?";
        params.add(count);
        Collection<Film> films = jdbcTemplate.query(sql, new FilmMapper(), params.toArray());

        return films;
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

    @Override
    public Collection<Film> getDirectorFilms(long directorId, String sortType) {
        String sqlQueryWithSortByLikes = """
                SELECT  f.FILM_ID,
                        f.name,
                        f.DESCRIPTION,
                        f.RELEASE_DATE,
                        f.DURATION,
                        f.rating_id,
                        r.rating_name,
                        COUNT(user_id) AS likes
                FROM director_films df
                JOIN films f ON f.film_id = df.film_id
                JOIN user_liked_films ulf ON f.film_id = ulf.film_id
                JOIN rating r ON f.rating_id = r.rating_id
                WHERE director_id = ?
                GROUP BY f.FILM_ID
                ORDER BY likes DESC
                """;

        String sqlQueryWithSortByYears = """
                SELECT *
                FROM director_films df
                JOIN films f ON df.film_id = f.film_id
                WHERE director_id = ?
                GROUP BY f.film_id
                ORDER BY f.release_date ASC
                """;

        if (Objects.equals(sortType, "likes")) {
            return jdbcTemplate.query(sqlQueryWithSortByLikes, new FilmMapper(), directorId);
        } else if (Objects.equals(sortType, "year")) {
            return jdbcTemplate.query(sqlQueryWithSortByYears, new FilmMapper(), directorId);
        } else {
            throw new NoSuchElementException("Отсортировать можно только по годам (year) и лайкам (likes)");
        }
    }
}