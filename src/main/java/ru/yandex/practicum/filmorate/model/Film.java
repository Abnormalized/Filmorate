package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validator.NotBeforeDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotBeforeDate(value = "1895-12-28", message = "Дата не может быть раньше дня создания кинемотографии")
    LocalDate releaseDate;
    @NotNull
    @DurationMin
    Duration duration;
    Rating mpa;
    List<Genre> genres;

    public long getDuration() {
        return duration.toSeconds();
    }
}