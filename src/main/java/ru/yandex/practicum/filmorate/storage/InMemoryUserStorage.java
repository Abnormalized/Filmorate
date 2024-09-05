package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    public User create(User user) {
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

    public long newId() {
        log.trace("Запрос на генерацию нового ID");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Выданный ID: {}", (currentMaxId + 1));
        return ++currentMaxId;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User update(User userNewInfo) {
        log.debug("Запрос на обновление существующего пользователя");
        if (!users.containsKey(userNewInfo.getId())) {
            log.warn("Запрашиваемый пользователь с ID: {} не найден", userNewInfo.getId());
            throw new NullPointerException();
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
}