package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    long reviewId;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    long useful;
    @NotNull
    Boolean isPositive;
    @NotBlank
    String content;
}
