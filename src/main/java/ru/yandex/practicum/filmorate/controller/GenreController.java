package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    public final GenreService genreService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Genre> findAll() {
        return genreService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Genre getFilmById(@PathVariable int id) {
        return genreService.getById(id);
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