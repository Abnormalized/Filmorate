package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validator.NotBeforeDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
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
    @NotNull
    Rating mpa;
    List<Genre> genres = new ArrayList<>();
    List<Director> directors;

    public long getDuration() {
        return duration.toSeconds();
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}