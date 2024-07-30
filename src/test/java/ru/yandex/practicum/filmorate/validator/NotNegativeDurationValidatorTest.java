package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotNegativeDurationValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validDurationShouldPass() {
        Film film = new Film("Аркейн", "Лучший мультфильм вселенной",
                LocalDate.of(2021, 11, 6), Duration.ofSeconds(22380));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidDateShouldFail() {
        Film film = new Film("Свадебная ваза", "Артхаус, который мы не заслуживаем, но получили",
                LocalDate.of(1974, 4, 11), Duration.ofSeconds(-4860));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroSecondFilmShouldPass() {
        Film film = new Film("Одна сотая секунды", "В фильме рассказывается о военном фотографе, " +
                "который сталкивается с профессиональной дилеммой...",
                LocalDate.of(2006, 8, 21), Duration.ofSeconds(0));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void theLastDisallowedDayShouldFail() {
        Film film = new Film("Назад в будущее", "Подросток Марти с помощью машины времени, сооружённой его " +
                "другом-профессором доком Брауном, попадает из 80-х в далекие 50-е.",
                LocalDate.of(1985, 7, 3), Duration.ofSeconds(-1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}