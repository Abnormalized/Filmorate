package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {
        Collection<Film> allFilms = Film.findAll();
        return new ResponseEntity<>(allFilms, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        Film newFilm = Film.create(film);
        return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film filmNewInfo) {
        Film updatedFilm = Film.update(filmNewInfo);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }
}