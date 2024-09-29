package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.*;

@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorDbStorage implements DirectorStorage {

    final JdbcTemplate jdbcTemplate;
    final RowMapper<Director> directorMapper;

    static final String GET_DIRECTORS = "SELECT * FROM directors";
    static final String GET_DIRECTOR_BY_ID = "SELECT * FROM directors WHERE director_id = ?";
    static final String GET_DIRECTOR_BY_NAME = "SELECT * FROM directors WHERE name = ? LIMIT(1)";
    static final String CREATE_DIRECTOR = "INSERT INTO directors(name) VALUES (?)";
    static final String UPDATE_DIRECTOR_NAME = "UPDATE directors SET name = ? WHERE director_id = ?";
    static final String DELETE_DIRECTOR_BY_ID = "DELETE FROM directors WHERE director_id = ?";
    static final String GET_COUNT_OF_DIRECTORS = "SELECT COUNT(director_id) FROM directors";
    static final String SET_DIRECTOR_FILM = "INSERT INTO director_films(director_id, film_id) VALUES (?, ?)";
    static final String DELETE_FILM_DIRECTORS = "DELETE FROM director_films WHERE film_id = ?";
    static final String FIND_PAIR_DIRECTOR_FILM = "SELECT * FROM director_films WHERE director_id = ? AND film_id = ?";
    static final String GET_FILM_DIRECTORS = """
            SELECT df.director_id,
                   d.name AS director_name
            FROM director_films df
            JOIN directors d ON d.director_id = df.director_id
            WHERE film_id = ?
            """;

    @Override
    public Collection<Director> findAll() {
        return jdbcTemplate.query(GET_DIRECTORS, directorMapper);
    }

    @Override
    public Optional<Director> getById(long id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(GET_DIRECTOR_BY_ID, directorMapper, id)
            );
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Director create(Director director) {
        jdbcTemplate.update(CREATE_DIRECTOR, director.getName());

        return jdbcTemplate.queryForObject(GET_DIRECTOR_BY_NAME,
                directorMapper, director.getName());
    }

    @Override
    public Director update(Director directorNewInfo) {
        getById(directorNewInfo.getId());

        jdbcTemplate.update(UPDATE_DIRECTOR_NAME, directorNewInfo.getName(), directorNewInfo.getId());

        return directorNewInfo;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_DIRECTOR_BY_ID, id);
    }

    @Override
    public int getCountOfDirectors() {
        Integer num = jdbcTemplate.queryForObject(GET_COUNT_OF_DIRECTORS, Integer.class);
        return Objects.requireNonNullElse(num, 0);
    }

    @Override
    public void saveDirectorsInfo(long filmId, List<Director> directors) {
        if (directors != null) {
            resetDirectorsInfoInFilm(filmId);
            for (Director director : directors) {
                if (!containsDirectorFilm(director.getId(), filmId)) {
                    jdbcTemplate.update(SET_DIRECTOR_FILM, director.getId(), filmId);
                }
            }
        }
    }

    private void resetDirectorsInfoInFilm(long filmId) {
        jdbcTemplate.update(DELETE_FILM_DIRECTORS, filmId);
    }

    @Override
    public boolean containsDirectorFilm(long directorId, long filmId) {
        return !jdbcTemplate.queryForList(FIND_PAIR_DIRECTOR_FILM, directorId, filmId).isEmpty();
    }

    @Override
    public void setFilmDirectors(Film film) {
        film.setDirectors(new ArrayList<>(jdbcTemplate.query(GET_FILM_DIRECTORS, directorMapper, film.getId())));
    }
}