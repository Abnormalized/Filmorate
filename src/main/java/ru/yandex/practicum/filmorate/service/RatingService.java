package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Collection<Rating> getAll() {
        return ratingStorage.getAll();
    }

    public Rating getById(long id) {
        return ratingStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Рейтинг с id " + id + " не найден"));
    }

    public void validateMpa(long id) {
        try {
            getById(id);
        } catch (NoSuchElementException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}