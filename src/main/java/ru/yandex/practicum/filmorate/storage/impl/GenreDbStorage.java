package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreMapper;

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", genreMapper);
    }

    @Override
    public Optional<Genre> getById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?", genreMapper, id)
            );
        } catch (Exception exception) {
            return Optional.empty();
        }
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
                genreMapper, film.getId())));
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

    public void loadGenres(Collection<Film> films) {

        if (!films.isEmpty()) {
            String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
            final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
            jdbcTemplate.query("SELECT * FROM GENRES G, FILMS_GENRE FG WHERE FG.GENRE_ID = G.GENRE_ID AND FG.FILM_ID IN (#ids)"
                    .replace("#ids", inSql), (rs) -> {
                final Film film = filmById.get(rs.getLong("FILM_ID"));
                film.addGenre(genreMapper.mapRow(rs, 0));
            }, films.stream().map(Film::getId).toArray());
        }
    }

    public List<Genre> findAllByIdArray(int[] idArray) {
        if (idArray.length == 0) {
            return Collections.emptyList();
        }
        String inSql = String.join(",", Collections.nCopies(idArray.length, "?"));
        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT G.* FROM GENRES G WHERE G.GENRE_ID IN (#ids)".replace("#ids", inSql));
            for (int idx = 0; idx < idArray.length; idx++) {
                ps.setObject(idx + 1, idArray[idx]);
            }
            return ps;
        }, genreMapper);
    }
}