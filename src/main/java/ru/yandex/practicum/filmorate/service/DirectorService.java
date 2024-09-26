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
        return directorStorage.getById(id).orElseThrow(NoSuchElementException::new);
    }

    public Director create(Director director) {
        if (director == null) {
            throw new ValidationException();
        }
        return directorStorage.create(director);
    }

    public Director update(Director directorNewInfo) {
        validateDirectorsId(List.of(directorNewInfo));
        return directorStorage.update(directorNewInfo);
    }

    public void delete(long id) {
        directorStorage.delete(id);
    }

    void validateDirectorsId(List<Director> directors) {
        if (directors != null) {
            int maxId = directorStorage.getCountOfDirectors();
            for (Director director : directors) {
                if (director.getId() > maxId) {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    void setFilmDirectors(Film film) {
        directorStorage.setFilmDirectors(film);
    }
}