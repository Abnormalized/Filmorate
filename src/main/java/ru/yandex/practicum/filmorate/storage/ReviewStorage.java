package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage {

    public Collection<Review> getReviewsByFilmId(long id, long count);

    public Review getReviewById(long id);

    public Review addReview(Review review);

    public Review updateReview(Review review);

    public Review addLike(long reviewId, long userId);

    public Review addDislike(long reviewId, long userId);

    public Review deleteReviewById(long id);

    public Review deleteLike(long reviewId, long userId);

    public Review deleteDislike(long reviewId, long userId);
}