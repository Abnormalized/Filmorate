package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    public Collection<Review> getAllReviews(long count);

    public Collection<Review> getReviewsByFilmId(long id, long count);

    public Optional<Review> getReviewById(long id);

    public Review addReview(Review review);

    public Review updateReview(Review review);

    public Review addLike(long reviewId, long userId);

    public Review addDislike(long reviewId, long userId);

    public void deleteReviewById(long id);

    public Review deleteLike(long reviewId, long userId);

    public Review deleteDislike(long reviewId, long userId);
}