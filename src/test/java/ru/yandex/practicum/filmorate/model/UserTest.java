package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.*;

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
            User user = new User();
            user.setEmail("email@mail.mail");
            user.setLogin("MyLogin");
            user.setName("MyName");
            user.setBirthday(LocalDate.of(1999, 2, 26));
            User.create(user);
        }

        Assertions.assertEquals(creatingTimes, User.findAll().size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        User lastAddedUser = null;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User();
            user.setEmail("email@mail.mail");
            user.setLogin("MyLogin");
            user.setName("MyName");
            user.setBirthday(LocalDate.of(1999, 2, 26));
            lastAddedUser = User.create(user);
        }

        Assertions.assertEquals(creatingTimes, lastAddedUser.getId());
    }

}