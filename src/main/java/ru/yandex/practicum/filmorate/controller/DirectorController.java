package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Director> findAll() {
        return directorService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Director getDirectorById(@PathVariable long id) {
        return directorService.getDirectorById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Director create(@RequestBody(required = false) Director director) {
        return directorService.create(director);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public Director update(@RequestBody Director directorNewInfo) {
        return directorService.update(directorNewInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        directorService.delete(id);
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