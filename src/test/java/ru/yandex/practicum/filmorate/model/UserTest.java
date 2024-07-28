package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserTest {

    @BeforeEach
    void setUp() {
        User.users.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User("email@mail.mail", "MyLogin", "MyName", LocalDate.of(1999, 2, 26));
            User.create(user);
        }

        Assertions.assertEquals(creatingTimes, User.findAll().size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        User lastAddedUser = null;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User("email@mail.mail", "MyLogin", "MyName", LocalDate.of(1999, 2, 26));
            lastAddedUser = User.create(user);
        }

        Assertions.assertEquals(creatingTimes, lastAddedUser.getId());
    }

}