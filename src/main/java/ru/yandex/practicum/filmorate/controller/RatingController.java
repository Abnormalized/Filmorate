package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

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
}