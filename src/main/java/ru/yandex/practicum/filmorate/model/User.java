package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.validator.NotContains;

import java.time.LocalDate;
import java.util.*;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    public static final Map<Long, User> users = new HashMap<>();

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

    public static Collection<User> findAll() {
        return users.values();
    }

    public static User create(User user) {
        log.debug("Запрос на создание пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя было выдано автоматически");
            user.setName(user.getLogin());
        }
        user.setId(newId());
        users.put(user.getId(), user);
        log.info("Пользователь {} создан", user.getLogin());
        return user;
    }

    public static User update(User userNewInfo) {
        log.debug("Запрос на обновление существующего пользователя");
        if (!users.containsKey(userNewInfo.getId())) {
            log.warn("Запрашиваемый пользователь с ID: {} не найден", userNewInfo.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        long id = userNewInfo.getId();
        User user = users.get(id);
        user.setLogin(Objects.requireNonNullElse(userNewInfo.getLogin(), user.getLogin()));
        user.setName(Objects.requireNonNullElse(userNewInfo.getName(), user.getName()));
        user.setEmail(userNewInfo.getEmail());
        if (user.getBirthday() != null) {
            user.setBirthday(Objects.requireNonNullElse(userNewInfo.getBirthday(), user.getBirthday()));
        } else {
            user.setBirthday(userNewInfo.getBirthday());
        }
        log.info("Обновлены данные о пользователе ID:{}, Name:{}", user.getId(), user.getName());
        return user;
    }

    private static long newId() {
        log.trace("Запрос на генерацию нового ID");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Выданный ID: {}", (currentMaxId + 1));
        return ++currentMaxId;
    }
}