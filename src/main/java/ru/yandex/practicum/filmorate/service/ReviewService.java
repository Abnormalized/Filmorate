package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FeedService feedService;
    private final ValidationService validationService;

    public Collection<Review> getReviewsByFilmId(long id, long count) {
        return id == 0 ? reviewStorage.getAllReviews(count) : reviewStorage.getReviewsByFilmId(id, count);
    }

    public Review getReviewById(long id) {
        return reviewStorage.getReviewById(id)
                .orElseThrow(() -> new NoSuchElementException("Отзыв с id " + id + " не найден"));
    }

    public Review addReview(Review review) {
        validationService.validateUserPresenceById(review.getUserId());
        validationService.validateFilmPresenceById(review.getFilmId());
        Review newReview = reviewStorage.addReview(review);
        feedService.addFeed(review.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId());
        return newReview;
    }

    public Review updateReview(Review review) {
        validationService.validateUserPresenceById(review.getUserId());
        validationService.validateFilmPresenceById(review.getFilmId());
        Review updatedReview = reviewStorage.updateReview(review);
        feedService.addFeed(review.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        return updatedReview;
    }

    public Review deleteReviewById(long id) {
        Review review = getReviewById(id);
        reviewStorage.deleteReviewById(id);
        feedService.addFeed(review.getUserId(), EventType.REVIEW, Operation.REMOVE, review.getReviewId());
        return review;
    }

    public Review manageLike(long reviewId, long userId, ReviewStorage.LikeManageAction action) {
        validationService.validateUserPresenceById(userId);
        Review review = getReviewById(reviewId);
        Review newReview = reviewStorage.manageLike(review, action);
        feedService.addFeed(userId, EventType.REVIEW, Operation.UPDATE, reviewId);
        return  newReview;
    }
}
