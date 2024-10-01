package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmService filmService;
    private final FeedService feedService;

    public User getUserById(long id) {
        return userStorage.getById(id);
    }

    public void validateUserPresenceById(long id) {
        userStorage.getById(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User userNewInfo) {
        return userStorage.update(userNewInfo);
    }

    public Collection<Film> getCommonFilms(long userId1, long userId2) {
        Collection<Long> filmsSet = new HashSet<>(userStorage.getLikedFilms(getUserById(userId1)));
        filmsSet.retainAll(userStorage.getLikedFilms(getUserById(userId2)));
        return filmsSet.stream()
                .map(filmService::getFilmById)
                .toList();
    }

    public void addUserToFriend(long userId1, long userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);
        if (getFriendList(userId1).contains(userId2)) {
            return;
        }
        if (userStorage.getAskedUsers(userId1).contains(userId2)) {
            userStorage.acceptFriend(user1, user2);
            return;
        }
        userStorage.addFriend(user1, user2);
    }

    public void deleteUserFromFriend(long userId1, long userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);
        if (userStorage.getById(userId2) == null) {
            throw new NoSuchElementException();
        }
        if (!userStorage.getAllFriends(getUserById(userId1)).contains(userId2)) {
            return;
        }
        userStorage.deleteFriend(user1, user2);
        feedService.addFeed(userId1, EventType.FRIEND, Operation.REMOVE, userId2);
    }

    public List<Long> getJointFriends(long userId1, long userId2) {

        return userStorage.getById(userId1).getUserFriends().stream()
                .filter(friendId -> userStorage.getById(userId2).getUserFriends().contains(friendId))
                .toList();
    }

    public List<User> getFriendList(long userId) {
        return userStorage.getAllFriends(getUserById(userId)).stream().map(this::getUserById).toList();
    }

    public Collection<Film> getRecommendations(long userId) {

        if (userStorage.getById(userId) == null) {
            throw new NoSuchElementException();
        }
        return filmService.getRecommendations(userId);
    }

    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }
}
