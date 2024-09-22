package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Collection<Rating> getAll() {
        return ratingStorage.getAll();
    }

    public Rating getById(int id) {
        return ratingStorage.getById(id).orElseThrow(NullPointerException::new);
    }

    void validateMpaId(long id) {
        if (id > ratingStorage.getCountOfMpa()) {
            throw new ValidationException();
        }
    }
}