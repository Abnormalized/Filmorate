package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotContainsValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validLoginShouldPass() {
        User user = new User("me@ya.ru", "Leather", "Jack", LocalDate.of(1999, 9, 9));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void loginWithWhitespaceShouldFail() {
        User user = new User("me@ya.ru", "Lea ther", "Jack", LocalDate.of(1999, 9, 9));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void loginWithSomeSymbolsShouldFail() {
        boolean testPassed = true;
        String someSymbols = "!@#$%^&*()_+|<,.>:;'[]{}-=";
        for (char symbol : someSymbols.toCharArray()) {
            String login = "Leather" + symbol;
            System.out.println(login);
            User user = new User("me@ya.ru", login, "Jack", LocalDate.of(1999, 9, 9));
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (violations.isEmpty()) {
                testPassed = false;
            }
        }
        assertTrue(testPassed);
    }

}