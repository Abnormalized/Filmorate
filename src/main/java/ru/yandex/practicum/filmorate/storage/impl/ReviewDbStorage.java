package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private static final String SQL_ADD_REVIEW = """
            INSERT INTO reviews (user_id, film_id, content, is_positive)
            VALUES (?, ?, ?, ?)
            """;
    private static final String SQL_UPD_REVIEW = """
            UPDATE reviews
            SET user_id = ?,
                film_id = ?,
                content = ?,
                is_positive = ?,
                useful_rating = ?
            WHERE review_id = ?
               OR (user_id = ? AND film_id = ?)
            """;
    private static final String SQL_DEL_REVIEW = """
            DELETE  FROM reviews
            WHERE review_id = ?
            """;
    private static final String SQL_GET_REVIEW = """
            SELECT *
            FROM reviews
            WHERE review_id = ?
            """;
    private static final String SQL_GET_ALL_REVIEWS = """
            SELECT *
            FROM reviews
            ORDER BY review_id DESC, useful_rating DESC
            LIMIT ?
            """;
    private static final String SQL_GET_ALL_REVIEWS_BY_FILM_ID = """
            SELECT *
            FROM reviews
            WHERE film_id = ?
            ORDER BY review_id DESC, useful_rating DESC
            LIMIT ?
            """;
    private static final String SQL_UPD_USEFUL = """
            UPDATE reviews
            SET useful_rating = ?
            WHERE review_id = ?
            """;

    private final JdbcTemplate jdbcT;
    private final RowMapper<Review> rowMapper;

    @Override
    public Collection<Review> getAllReviews(long count) {
        return jdbcT.query(SQL_GET_ALL_REVIEWS, rowMapper, count);
    }

    @Override
    public Collection<Review> getReviewsByFilmId(long id, long count) {
        return jdbcT.query(SQL_GET_ALL_REVIEWS_BY_FILM_ID, rowMapper, id, count);
    }

    @Override
    public Optional<Review> getReviewById(long id) {
        try {
            Review review = jdbcT.queryForObject(SQL_GET_REVIEW, rowMapper, id);
            return Optional.ofNullable(review);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Review addReview(Review review) {
        Object[] params = new Object[]{
                review.getUserId(),
                review.getFilmId(),
                review.getContent(),
                review.getIsPositive()
        };
        PreparedStatementCreator psCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_REVIEW, Statement.RETURN_GENERATED_KEYS);
            for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                ps.setObject(paramIndex + 1, params[paramIndex]);
            }
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcT.update(psCreator, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new RuntimeException("Ошибка при записи отзыва в базу данных");
        }
        review.setReviewId(id);

        return review;
    }

    @Override
    public Review updateReview(Review review) {
        int totalRowsUpdated = jdbcT.update(SQL_UPD_REVIEW,
                review.getUserId(),
                review.getFilmId(),
                review.getContent(),
                review.getIsPositive(),
                review.getUseful(),
                review.getReviewId(),
                review.getUserId(),
                review.getFilmId());
        if (totalRowsUpdated == 0) {
            throw new NoSuchElementException("Отзыв с id " + review.getReviewId() + " не найден");
        }
        return review;
    }

    @Override
    public void deleteReviewById(long id) {
        jdbcT.update(SQL_DEL_REVIEW, id);
    }

    @Override
    public Review manageLike(Review review, ReviewStorage.LikeManageAction action) {
        long useful = review.getUseful();
        switch (action) {
            case ADD_LIKE, DEL_DISLIKE -> ++useful;
            case DEL_LIKE -> --useful;
            case ADD_DISLIKE -> {
                --useful;
                if (useful == 0) {
                    --useful;
                }
            }
        }
        int totalRowsUpdated = jdbcT.update(SQL_UPD_USEFUL, useful, review.getReviewId());
        if (totalRowsUpdated == 0) {
            throw new NoSuchElementException("Отзыв с id " + review.getReviewId() + " не найден");
        }
        review.setUseful(useful);
        return review;
    }
}
