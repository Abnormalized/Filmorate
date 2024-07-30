package ru.yandex.practicum.filmorate.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The annotated element must not contain any character from given value string.
 * Accepts {@code String} type
 *
 * @author Zhora Avetisyan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {NotContainsValidator.class}
)
public @interface NotContains {

    String message() default "Содержит недопустимые символы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}