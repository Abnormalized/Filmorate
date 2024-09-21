package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {

    public final RatingService ratingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Rating> findAll() {
        return ratingService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Rating getFilmById(@PathVariable int id) {
        return ratingService.getById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationHandle(ValidationException e) {
        return new ErrorResponse("error", "Указаны некорректные данные.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NullPointerException e) {
        return new ErrorResponse("error", "Не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_EXTENDED)
    public ErrorResponse exceptionHandle(RuntimeException e) {
        return new ErrorResponse("error", "Ошибка сервера.");
    }
}