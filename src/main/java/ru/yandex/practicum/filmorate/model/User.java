package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(of = {"id", "login", "email"})
@Data
@Slf4j
public class User {
    public static final Map<Long, User> users = new HashMap<>();

    private long id;
    @Email(message = "Некорректный формат введеного email")
    @NotEmpty(message = "Поле email не может быть пустым")
    private String email;
    @NotBlank(message = "Поле login не может быть пустым")
    private String login;
    private String name;
    @Past(message = "Введена некорректная дата рождения")
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        if (login != null && (login.contains(" ") || login.isBlank())) {
            log.warn("Введено некорректное значение поля login");
            throw new ValidationException("Введен недопустимый логин");
        }
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public static Collection<User> findAll() {
        log.info("Запрос на выдачу всех пользователей");
        return users.values();
    }

    public static User create(User user) {
        log.info("Запрос на создание пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя было выдано автоматически");
            user.setName(user.getLogin());
        }
        user.setId(newId());
        users.put(user.getId(), user);
        log.trace("Пользователь создан");
        return user;
    }

    public static User update(User userNewInfo) {
        log.info("Запрос на обновление существующего пользователя");
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