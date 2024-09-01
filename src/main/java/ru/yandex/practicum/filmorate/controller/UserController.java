package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public User getUserById(@PathVariable long id) {
        return userStorage.users.get(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}/friends")
    public Set<User> getFriendList(@PathVariable long id) {
        return userStorage.users.get(id).getUserFriends().stream().map(this::getUserById).collect(Collectors.toSet());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getJointFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getJointFriends(id, otherId).stream().map(this::getUserById).collect(Collectors.toSet());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public User update(@Valid @RequestBody User userNewInfo) {
        return userStorage.update(userNewInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/friends/{friendId}")
    public void addToFriendsList(@PathVariable long id, @PathVariable long friendId) {
        userService.addUserToFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriendList(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteUserFromFriend(id, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationHandle(ValidationException e) {
        return new ErrorResponse("error", "Указаны некорректные данные.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NullPointerException e) {
        return new ErrorResponse("error", "Не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_EXTENDED)
    public ErrorResponse exceptionHandle(RuntimeException e) {
        return new ErrorResponse("error", "Ошибка сервера.");
    }
}