package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotContainsValidator implements ConstraintValidator<NotContains, String> {

    String notAllowedCharsString;

    @Override
    public void initialize(NotContains constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        notAllowedCharsString = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null) {
            for (Character anotherChar : notAllowedCharsString.toCharArray()) {
                if (value.contains(anotherChar.toString())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}