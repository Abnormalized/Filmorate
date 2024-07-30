package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class NotNegativeDurationValidator implements ConstraintValidator<NotNegativeDuration, Duration> {

    @Override
    public boolean isValid(Duration validatingValue, ConstraintValidatorContext constraintValidatorContext) {
        return !validatingValue.isNegative();
    }
}