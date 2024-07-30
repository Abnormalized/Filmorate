package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;

class FilmTest {

    @BeforeEach
    void setUp() {
        Film.films.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = new Film();
            film.setName("Name");
            film.setDescription("Description");
            film.setReleaseDate(LocalDate.of(1999, 2, 26));
            film.setDuration(Duration.ofSeconds(10));
            Film.create(film);
        }

        Assertions.assertEquals(creatingTimes, Film.findAll().size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        Film lastAddedFilm = null;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = new Film();
            film.setName("Name");
            film.setDescription("Description");
            film.setReleaseDate(LocalDate.of(1999, 2, 26));
            film.setDuration(Duration.ofSeconds(10));
            lastAddedFilm = Film.create(film);
        }

        Assertions.assertEquals(creatingTimes, lastAddedFilm.getId());
    }

}