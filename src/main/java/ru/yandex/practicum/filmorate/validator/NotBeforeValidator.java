package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotBeforeValidator implements ConstraintValidator<NotBeforeDate, LocalDate> {

    String minDate;

    @Override
    public void initialize(NotBeforeDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minDate = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate validatingValue, ConstraintValidatorContext constraintValidatorContext) {
        return !validatingValue.isBefore(LocalDate.parse(minDate));
    }
}