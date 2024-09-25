package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorMapper;

import java.util.*;

@Repository
@AllArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Director> findAll() {
        return jdbcTemplate.query("SELECT * FROM directors", new DirectorMapper());
    }

    @Override
    public Optional<Director> getById(long id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM directors WHERE director_id = ?",
                            new DirectorMapper(), id)
            );
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Director create(Director director) {
        jdbcTemplate.update("INSERT INTO directors(name) VALUES (?)", director.getName());

        return jdbcTemplate.queryForObject("SELECT * FROM directors WHERE name = ? LIMIT(1)",
                new DirectorMapper(), director.getName());
    }

    @Override
    public Director update(Director directorNewInfo) {
        String sqlQuery = """
                UPDATE directors SET
                name = ?
                WHERE director_id = ?
                """;

        getById(directorNewInfo.getId());

        jdbcTemplate.update(sqlQuery, directorNewInfo.getName(), directorNewInfo.getId());

        return directorNewInfo;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM directors WHERE director_id = ?", id);
    }

    @Override
    public int getCountOfDirectors() {
        Integer num = jdbcTemplate.queryForObject("SELECT COUNT(director_id) FROM directors",
                Integer.class);
        return Objects.requireNonNullElse(num, 0);
    }

    @Override
    public void saveDirectorsInfo(long filmId, List<Director> directors) {
        if (directors != null) {
            resetDirectorsInfoInFilm(filmId);
            String genreSqlQuery = """
                        INSERT INTO director_films(director_id, film_id)
                        VALUES (?, ?)
                """;
            for (Director director : directors) {
                if (!containsDirectorFilm(director.getId(), filmId)) {
                    jdbcTemplate.update(genreSqlQuery, director.getId(), filmId);
                }
            }
        }
    }

    private void resetDirectorsInfoInFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM director_films WHERE film_id = ?", filmId);
    }

    @Override
    public boolean containsDirectorFilm(long directorId, long filmId) {
        return !jdbcTemplate.queryForList("SELECT * FROM director_films WHERE director_id = ? AND film_id = ?",
                directorId, filmId).isEmpty();
    }

    @Override
    public void setFilmDirectors(Film film) {
        final String sqlQuery = """
                SELECT df.director_id,
                       d.name AS director_name
                FROM director_films df
                JOIN directors d ON d.director_id = df.director_id
                WHERE film_id = ?
            """;
        film.setDirectors(new ArrayList<>(jdbcTemplate.query(sqlQuery,
                new DirectorMapper(), film.getId())));
    }
}