package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", new GenreMapper());
    }

    @Override
    public Optional<Genre> getById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?", new GenreMapper(), id)
            );
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public List<Genre> findByIds(List<Long> genreIds) {
        final String query = "SELECT genre_id,genre_name FROM genres WHERE genre_id IN (?)";
        return jdbcTemplate.query(query, new GenreMapper(), genreIds);
    }

    @Override
    public void setFilmGenres(Film film) {
        final String sqlQuery = """
                    SELECT fg.genre_id,
                           g.name AS genre_name
                    FROM films_genre fg
                    JOIN genres g ON g.genre_id = fg.genre_id
                    WHERE film_id = ?
                """;
        film.setGenres(new ArrayList<>(jdbcTemplate.query(sqlQuery,
                new GenreMapper(), film.getId())));
    }

    @Override
    public void saveGenresInfo(long filmId, List<Genre> genres) {
        if (genres != null) {
            resetGenresInfoInFilm(filmId);
            String genreSqlQuery = """
                            INSERT INTO films_genre(film_id, genre_id)
                            VALUES (?, ?)
                    """;
            for (Genre genre : genres) {
                if (!containsFilmGenre(filmId, genre.getId())) {
                    jdbcTemplate.update(genreSqlQuery, filmId, genre.getId());
                }
            }
        }
    }

    private void resetGenresInfoInFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM films_genre WHERE film_id = ?", filmId);
    }

    @Override
    public boolean containsFilmGenre(long genreId, long filmId) {
        return !jdbcTemplate.queryForList("SELECT * FROM films_genre WHERE film_id = ? AND genre_id = ?",
                genreId, filmId).isEmpty();
    }

    @Override
    public int getCountOfGenres() {
        return jdbcTemplate.queryForObject("SELECT COUNT(genre_id) FROM genres", Integer.class);
    }
}