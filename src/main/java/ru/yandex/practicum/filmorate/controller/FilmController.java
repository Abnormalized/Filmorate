package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(name = "count", defaultValue = "10",
            required = false) Integer count,
                                           @RequestParam(name = "genreId", required = false) Integer genreId,
                                           @RequestParam(name = "year", required = false) Integer year) {
        return filmService.getPopularFilm(count, genreId, year);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/director/{director-id}")
    public Collection<Film> getDirectorFilms(@PathVariable(value = "director-id") long directorId,
                                             @PathParam("sortBy") String sortBy) {
        return filmService.getDirectorFilms(directorId, sortBy);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public Film update(@Valid @RequestBody Film filmNewInfo) {
        return filmService.update(filmNewInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{liked-film-id}/like/{user-id}")
    public void likeFilm(@PathVariable(value = "liked-film-id") long likedFilmId,
                         @PathVariable(value = "user-id") long userId) {
        filmService.addLike(userId, likedFilmId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{disliked-film-id}/like/{user-id}")
    public void removeLikeFromFilm(@PathVariable(value = "disliked-film-id") long dislikedFilmId,
                                   @PathVariable(value = "user-id") long userId) {
        filmService.removeLike(userId, dislikedFilmId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{filmId}")
    public void deleteFilmById(@PathVariable @Positive long filmId) {
        filmService.deleteFilmById(filmId);
    }
}