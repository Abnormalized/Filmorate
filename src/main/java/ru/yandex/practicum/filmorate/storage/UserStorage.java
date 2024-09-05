package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    User create(User user);

    long newId();

    Collection<User> findAll();

    User update(User userNewInfo);
}