package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationHandle(ValidationException e) {
        log.error("error", e.getMessage());
        return new ErrorResponse("error", "Указаны некорректные данные.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_EXTENDED)
    public ErrorResponse exceptionHandle(RuntimeException e) {
        log.error("error", e);
        System.out.println(e.getLocalizedMessage());
        return new ErrorResponse("error", "Ошибка сервера.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NullPointerException e) {
        log.error("error", e.getMessage());
        return new ErrorResponse("error", "Не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NoSuchElementException e) {
        log.error("error", e.getMessage());
        return new ErrorResponse("error", "Элемент не найден.");
    }

}
