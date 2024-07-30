package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotBeforeValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validDateShouldPass() {
        Film film = new Film();
        film.setName("Аркейн");
        film.setDescription("Лучший мультфильм вселенной");
        film.setReleaseDate(LocalDate.of(2021, 11, 6));
        film.setDuration(Duration.ofSeconds(22380));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidDateShouldFail() {
        Film film = new Film();
        film.setName("Свадебная ваза");
        film.setDescription("Артхаус, который мы не заслуживаем, но получили");
        film.setReleaseDate(LocalDate.of(1874, 4, 11));
        film.setDuration(Duration.ofSeconds(4860));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void theFirstAllowedDayShouldPass() {
        Film film = new Film();
        film.setName("Прибытие поезда на вокзал Ла-Сьота́");
        film.setDescription("Первый фильм в истории человечества");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofSeconds(48));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void theLastDisallowedDayShouldFail() {
        Film film = new Film();
        film.setName("SCP-1337");
        film.setDescription("Засекреченные материалы съемок земли инопланетными цивилизациями");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(Duration.ofSeconds(666));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

}