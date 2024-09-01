package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.NotContains;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;
    @Email
    @NotEmpty
    String email;
    @NotBlank
    @NotContains(value = " !@#$%^&*()_+|<,.>:;'[]{}-=")
    String login;
    String name;
    @Past
    LocalDate birthday;
    Set<Long> userFriends = new HashSet<>();
    Set<Long> likedFilms = new HashSet<>();
}