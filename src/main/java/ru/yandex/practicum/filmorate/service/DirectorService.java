package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Collection<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director getDirectorById(long id) {
        return directorStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Режиссер с id " + id + " не найден"));
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director directorNewInfo) {
        return directorStorage.update(directorNewInfo);
    }

    public void delete(long id) {
        directorStorage.delete(id);
    }

    void validateDirectors(List<Director> directors) {
        try {
            directors.forEach(director -> getDirectorById(director.getId()));
        } catch (NoSuchElementException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    void setFilmDirectors(Film film) {
        directorStorage.setFilmDirectors(film);
    }

    public void loadDirectors(Collection<Film> films) {
        directorStorage.loadDirectors(films);
    }
}