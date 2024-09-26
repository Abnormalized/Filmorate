package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    ReviewStorage reviewStorage;
    UserService userService;
    FilmService filmService;

    public Collection<Review> getReviewsByFilmId(long id, long count) {
        return List.of();
    }

    public Review getReviewById(long id) {
        return null;
    }

    public Review addReview(Review review) {
        return null;
    }

    public Review updateReview(Review review) {
        return null;
    }

    public Review addLike(long reviewId, long userId) {
        return null;
    }

    public Review addDislike(long reviewId, long userId) {
        return null;
    }

    public Review deleteReviewById(long id) {
        return null;
    }

    public Review deleteLike(long reviewId, long userId) {
        return null;
    }

    public Review deleteDislike(long reviewId, long userId) {
        return null;
    }
}
