package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    enum LikeManageAction {
        ADD_LIKE,
        DEL_LIKE,
        ADD_DISLIKE,
        DEL_DISLIKE
    }

    Collection<Review> getAllReviews(long count);

    Collection<Review> getReviewsByFilmId(long id, long count);

    Optional<Review> getReviewById(long id);

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReviewById(long id);

    Review manageLike(Review review, LikeManageAction action);
}