package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import lombok.AllArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    @GetMapping
    public Collection<Review> getReviewsByFilmId(@RequestParam(defaultValue = "-1") long id,
                                                 @RequestParam(defaultValue = "10") @Positive long count) {
        return service.getReviewsByFilmId(id, count);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        return service.getReviewById(id);
    }

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review) {
        return service.addReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        return service.updateReview(review);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLike(@PathVariable("id") @Positive long reviewId, @PathVariable @Positive long userId) {
        return service.addLike(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review addDislike(@PathVariable("id") @Positive long reviewId, @PathVariable @Positive long userId) {
        return service.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{id}")
    public Review deleteReviewById(@PathVariable @Positive long id) {
        return service.deleteReviewById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLike(@PathVariable("id") @Positive long reviewId, @PathVariable @Positive long userId) {
        return service.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislike(@PathVariable("id") @Positive long reviewId, @PathVariable @Positive long userId) {
        return service.deleteDislike(reviewId, userId);
    }
}
