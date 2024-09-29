package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(int id) {
        return genreStorage.getById(id).orElseThrow(NoSuchElementException::new);
    }

    void validateGenreId(List<Genre> genres) {
        if (genres != null) {
            int maxId = genreStorage.getCountOfGenres();
            for (Genre genre : genres) {
                if (genre.getId() > maxId) {
                    throw new ValidationException();
                }
            }
        }
    }

    void setFilmGenres(Film film) {
        genreStorage.setFilmGenres(film);
    }

    public void loadGenres(Collection<Film> films) {
        genreStorage.loadGenres(films);
    }
}