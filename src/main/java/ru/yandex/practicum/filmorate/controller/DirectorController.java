package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;


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
}