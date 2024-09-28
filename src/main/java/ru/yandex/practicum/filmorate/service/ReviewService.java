package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ReviewService {
    ReviewStorage reviewStorage;
    UserService userService;
    FilmService filmService;

    public Collection<Review> getReviewsByFilmId(long id, long count) {
        return id == -1 ? reviewStorage.getAllReviews(count) : reviewStorage.getReviewsByFilmId(id, count);
    }

    public Review getReviewById(long id) {
        return reviewStorage.getReviewById(id)
                .orElseThrow(() -> new NoSuchElementException("Отзыв с id " + id + " не найден"));
    }

    public Review addReview(Review review) {
        userService.validateUserPresenceById(review.getUserId());
        filmService.validateFilmPresenceById(review.getFilmId());
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        userService.validateUserPresenceById(review.getUserId());
        filmService.validateFilmPresenceById(review.getFilmId());
        return reviewStorage.updateReview(review);
    }

    public Review deleteReviewById(long id) {
        Review review = getReviewById(id);
        reviewStorage.deleteReviewById(id);
        return review;
    }

    public Review manageLike(long reviewId, long userId, ReviewStorage.LikeManageAction action) {
        userService.validateUserPresenceById(userId);
        Review review = getReviewById(reviewId);
        return reviewStorage.manageLike(review, action);
    }
}
