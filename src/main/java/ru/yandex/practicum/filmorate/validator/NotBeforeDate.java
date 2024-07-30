package ru.yandex.practicum.filmorate.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The annotated element must not be before date contained into value.
 * Accepts {@code localDate} type
 *
 * @author Zhora Avetisyan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {NotBeforeValidator.class}
)
public @interface NotBeforeDate {

    String message() default "Выставлена недопустимая дата";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}