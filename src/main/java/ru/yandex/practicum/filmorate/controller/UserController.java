package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}/friends")
    public List<User> getFriendList(@PathVariable long id) {
        return userService.getFriendList(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getJointFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getJointFriends(id, otherId).stream().map(this::getUserById).collect(Collectors.toSet());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public User update(@Valid @RequestBody User userNewInfo) {
        return userService.update(userNewInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/friends/{friend-id}")
    public void addToFriendsList(@PathVariable long id, @PathVariable(value = "friend-id") long friendId) {
        userService.addUserToFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}/friends/{friend-id}")
    public void removeFromFriendList(@PathVariable long id, @PathVariable(value = "friend-id") long friendId) {
        userService.deleteUserFromFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable @Positive long userId) {
        userService.deleteUserById(userId);
    }
}