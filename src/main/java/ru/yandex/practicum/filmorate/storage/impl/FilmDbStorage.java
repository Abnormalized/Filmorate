package ru.yandex.practicum.filmorate.storage.impl;

import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDbStorage implements FilmStorage {
    final JdbcTemplate jdbcTemplate;
    final GenreStorage genreStorage;
    final DirectorStorage directorStorage;
    final RowMapper<Film> rowMapper;

    private static final String sqlQueryRecommendations = """
                            SELECT FILMS.*, RATING.RATING_NAME
                            FROM FILMS
                            JOIN (
                              SELECT other_users.USER_ID, other_users.FILM_ID
                              FROM USER_LIKED_FILMS other_users
                              LEFT JOIN (
                                SELECT FILM_ID, USER_ID
                                FROM USER_LIKED_FILMS
                                WHERE USER_ID = ?
                              ) this_user ON other_users.FILM_ID = this_user.FILM_ID
                              WHERE other_users.USER_ID <> ?
                                AND this_user.USER_ID IS NULL
                            ) NOT_LIKED_COUNT ON NOT_LIKED_COUNT.FILM_ID = FILMS.film_id
                            JOIN (
                              SELECT other_users.USER_ID, COUNT(other_users.FILM_ID) AS film_count
                              FROM USER_LIKED_FILMS this_user
                              JOIN USER_LIKED_FILMS other_users ON this_user.FILM_ID = other_users.FILM_ID
                              WHERE this_user.USER_ID = ?
                                AND other_users.USER_ID <> this_user.USER_ID
                              GROUP BY other_users.USER_ID
                            ) COMMON_LIKES_COUNT ON NOT_LIKED_COUNT.user_id = COMMON_LIKES_COUNT.user_id
                            JOIN (
                              SELECT USER_LIKED_FILMS.FILM_ID, COUNT(USER_LIKED_FILMS.USER_ID) AS like_count
                              FROM USER_LIKED_FILMS
                              GROUP BY USER_LIKED_FILMS.FILM_ID
                            ) LIKES_COUNT ON FILMS.film_id = LIKES_COUNT.film_id
            LEFT JOIN RATING ON FILMS.rating_id = RATING.rating_id
                            GROUP BY FILMS.film_id, FILMS.description, FILMS.duration, FILMS.name, FILMS.rating_id, FILMS.release_date
                            ORDER BY COMMON_LIKES_COUNT.film_count, LIKES_COUNT.like_count""";

    private static final String SQL_SEARCH_BEG = """
               SELECT f.*
               FROM films f
               LEFT JOIN user_liked_films l ON l.film_id = f.film_id
               LEFT JOIN director_films df ON df.film_id = f.film_id
               LEFT JOIN directors d ON d.director_id = df.director_id
               WHERE
            """;
    private static final String SQL_SEARCH_END = """
            GROUP BY f.film_id
            ORDER BY COUNT(l.film_id) DESC
            """;

    @Override
    public Optional<Film> getById(long id) {
        try {
            Film film = jdbcTemplate.queryForObject("SELECT films.*, RATING.rating_name  FROM  films " +
                    "LEFT JOIN RATING ON FILMS.rating_id = RATING.rating_id" +
                    " WHERE film_id = ?", rowMapper, id);

            genreStorage.setFilmGenres(film);

            return Optional.ofNullable(film);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
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
                SELECT FILMS.*, RATING.rating_name FROM films
                LEFT JOIN RATING ON FILMS.rating_id = RATING.rating_id
                WHERE name = ? AND release_date = ?
                ORDER BY film_id DESC
                LIMIT(1)""";

        Film filmFromDb = jdbcTemplate.queryForObject(returnSqlQuery, rowMapper, film.getName(), film.getReleaseDate());

        List<Genre> genries = null;

        if (film.getGenres() != null) {

            genries = genreStorage.findAllByIdArray(film.getGenres().stream().map(Genre::getId).mapToInt(Integer::intValue).toArray());

            checkGenres(genries, film.getGenres());
        }

        genreStorage.saveGenresInfo(filmFromDb.getId(), film.getGenres());
        directorStorage.saveDirectorsInfo(filmFromDb.getId(), film.getDirectors());

        Optional<Film> returnFilm = getById(filmFromDb.getId());

        if (returnFilm.isEmpty()) {
            throw new NoSuchElementException("Фильм с id " + filmFromDb.getId() + " не найден");
        }

        Film newFilm = returnFilm.get();

        newFilm.setGenres(genries);

        if (newFilm.getGenres() == null) {
            newFilm.setGenres(new ArrayList<>());
        }
        return newFilm;
    }

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query("SELECT films.*, RATING.rating_name FROM films" +
                " LEFT JOIN RATING ON FILMS.rating_id = RATING.rating_id", rowMapper);
    }

    @Override
    public Collection<Film> findAll(String searchQuery, String by) {
        searchQuery = "%" + searchQuery + "%";
        String sqlSearchMid = ".name LIKE ?";
        int paramsCnt = 1;

        switch (by) {
            case "title" -> sqlSearchMid = "f" + sqlSearchMid;
            case "director" -> sqlSearchMid = "d" + sqlSearchMid;
            case "title,director", "director,title" -> {
                sqlSearchMid = "f" + sqlSearchMid + " OR " + "d" + sqlSearchMid;
                ++paramsCnt;
            }
            default -> throw new RuntimeException("Невалидные параметры запроса " + by);
        }

        String sqlSearch = SQL_SEARCH_BEG + sqlSearchMid + SQL_SEARCH_END;
        return jdbcTemplate.query(sqlSearch, rowMapper, Collections.nCopies(paramsCnt, searchQuery).toArray());
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
                SELECT films.*, RATING.rating_name
                FROM films
                LEFT JOIN RATING ON FILMS.rating_id = RATING.rating_id
                WHERE name = ? AND release_date = ?
                ORDER BY film_id DESC
                LIMIT(1)""";

        Film filmFromDb = jdbcTemplate.queryForObject(returnSqlQuery, rowMapper,
                filmNewInfo.getName(), filmNewInfo.getReleaseDate());

        List<Genre> genries = null;

        if (filmNewInfo.getGenres() != null) {

            genries = genreStorage.findAllByIdArray(filmNewInfo.getGenres().stream().map(Genre::getId).mapToInt(Integer::intValue).toArray());

            checkGenres(genries, filmNewInfo.getGenres());
        }

        genreStorage.saveGenresInfo(filmFromDb.getId(), filmNewInfo.getGenres());
        directorStorage.saveDirectorsInfo(filmFromDb.getId(), filmNewInfo.getDirectors());

        Optional<Film> returnFilm = getById(filmFromDb.getId());

        if (returnFilm.isEmpty()) {
            throw new NoSuchElementException("Фильм с id " + filmFromDb.getId() + " не найден");
        }

        Film newFilm = returnFilm.get();

        newFilm.setGenres(genries);

        if (newFilm.getGenres() == null) {
            newFilm.setGenres(new ArrayList<>());
        }
        return newFilm;
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        String sql = "SELECT f.*, RATING.rating_name, COUNT(l.film_id) AS like_count " +
                "FROM films f " +
                "LEFT JOIN RATING ON f.rating_id = RATING.rating_id " +
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

        return jdbcTemplate.query(sql, rowMapper, params.toArray());
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
            return jdbcTemplate.query(sqlQueryWithSortByLikes, rowMapper, directorId);
        } else if (Objects.equals(sortType, "year")) {
            return jdbcTemplate.query(sqlQueryWithSortByYears, rowMapper, directorId);
        } else {
            throw new ValidationException("Отсортировать можно только по годам (year) и лайкам (likes)");
        }
    }

    @Override
    public Collection<Film> getRecommendations(long userId) {

        return jdbcTemplate.query(sqlQueryRecommendations, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(Duration.ofSeconds(rs.getInt("duration")));
            Rating rating = new Rating();
            rating.setId(rs.getLong("rating_id"));
            rating.setName(rs.getString("rating_name"));
            film.setMpa(rating);
            return film;
        }, userId, userId, userId);
    }

    private void checkGenres(List<Genre> genries, List<Genre> genres) {
        List<Integer> requestGenresIds = genres.stream().map(Genre::getId).toList();
        List<Integer> dbGenresIds = genries.stream().map(Genre::getId).toList();

        for (Integer requestGenreId : requestGenresIds) {

            if (!dbGenresIds.contains(requestGenreId)) {
                throw new NoSuchElementException(MessageFormat.format("Жанр с id {0, number} не найден", requestGenreId));
            }
        }
    }

    @Override
    public void deleteFilmById(long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
