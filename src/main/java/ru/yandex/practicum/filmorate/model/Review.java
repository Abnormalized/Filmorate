package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Review {
    private long reviewId;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private long useful;
    @NotNull
    private Boolean isPositive;
    @NotBlank
    private String content;
}
