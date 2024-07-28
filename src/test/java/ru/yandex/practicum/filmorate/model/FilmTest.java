package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

class FilmTest {

    @BeforeEach
    void setUp() {
        Film.films.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = Film.create(new Film("Name", "Description", LocalDate.of(1999, 2, 1), Duration.ofSeconds(100)));
            Film.create(film);
        }

        Assertions.assertEquals(creatingTimes, Film.findAll().size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        Film lastAddedFilm = null;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = Film.create(new Film("Name", "Description",
                    LocalDate.of(1999, 2, 1), Duration.ofSeconds(new Random().nextInt(1000))));
            lastAddedFilm = Film.create(film);
        }

        Assertions.assertEquals(creatingTimes, lastAddedFilm.getId());
    }

}