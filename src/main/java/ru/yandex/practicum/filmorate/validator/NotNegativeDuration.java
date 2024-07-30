package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The annotated element must not be negative.
 * Accepts {@code Duration} type
 *
 * @author Zhora Avetisyan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {NotNegativeDurationValidator.class}
)
public @interface NotNegativeDuration {

    String message() default "Не может быть отрицательным";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}