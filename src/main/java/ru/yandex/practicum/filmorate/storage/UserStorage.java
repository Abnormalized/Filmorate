package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    Optional<User> getById(long id);

    User create(User user);

    Collection<User> findAll();

    User update(User userNewInfo);

    Set<Long> getAllFriends(User user);

    Set<Long> getAcceptedFriends(long userId);

    Set<Long> getAskedFriends(long userId);

    Set<Long> getAskedUsers(long userId);

    boolean addFriend(User user, User friend);

    boolean acceptFriend(User user, User friend);

    boolean deleteFriend(User user, User friend);

    Set<Long> getLikedFilms(User user);

    void deleteUserById(long id);;
}