package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        Collection<User> users = User.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User newUser = User.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User userNewInfo) {
        User updatedUser = User.update(userNewInfo);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}