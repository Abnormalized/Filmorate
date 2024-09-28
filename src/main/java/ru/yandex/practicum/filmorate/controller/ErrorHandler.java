package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationHandle(ValidationException e) {
        log.error("validationHandle - {}", e.getMessage());
        return new ErrorResponse("error", "Указаны некорректные данные.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_EXTENDED)
    public ErrorResponse exceptionHandle(RuntimeException e) {
        log.error("exceptionHandle - {}", e.getMessage());
        System.out.println(e.getLocalizedMessage());
        return new ErrorResponse("error", "Ошибка сервера.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NoSuchElementException e) {
        log.error("notFoundHandle - {}", e.getMessage());
        return new ErrorResponse("error", "Ничего не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse invalidHttpMethodArgumentTypeHandle(MethodArgumentTypeMismatchException e) {
        log.error("invalidHttpMethodArgumentTypeHandle - {}", e.getMessage());
        return new ErrorResponse("ничего не найдено", "параметры HTTP запроса невалидны: "
                + e.getParameter().getParameterName() + " = " + e.getValue());
    }
}
