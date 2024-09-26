package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;

@Repository
@AllArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcT;
    private final RowMapper<Review> rowMapper;

    @Override
    public Collection<Review> getReviewsByFilmId(long id, long count) {
        return List.of();
    }

    @Override
    public Review getReviewById(long id) {
        return null;
    }

    @Override
    public Review addReview(Review review) {
        return null;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public Review addLike(long reviewId, long userId) {
        return null;
    }

    @Override
    public Review addDislike(long reviewId, long userId) {
        return null;
    }

    @Override
    public Review deleteReviewById(long id) {
        return null;
    }

    @Override
    public Review deleteLike(long reviewId, long userId) {
        return null;
    }

    @Override
    public Review deleteDislike(long reviewId, long userId) {
        return null;
    }
}
