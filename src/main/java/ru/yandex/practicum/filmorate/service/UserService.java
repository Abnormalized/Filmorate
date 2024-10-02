package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmService filmService;
    private final FeedService feedService;

    public User getUserById(long id) {

        User user = getUserIfExists(id);
        user.setUserFriends(userStorage.getAllFriends(user));
        user.setLikedFilms(userStorage.getLikedFilms(user));

        return user;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        checkUserName(user);
        return userStorage.create(user);
    }

    public User update(User userNewInfo) {
        getUserIfExists(userNewInfo.getId());
        checkUserName(userNewInfo);
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
        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);
        if (user1.getUserFriends().contains(userId2)) {
            return;
        }
        if (userStorage.getAskedUsers(userId1).contains(userId2)) {
            userStorage.acceptFriend(user1, user2);
            feedService.addFeed(userId1, EventType.FRIEND, Operation.UPDATE, userId2);
            return;
        }
        userStorage.addFriend(user1, user2);
        feedService.addFeed(userId1, EventType.FRIEND, Operation.ADD, userId2);
    }

    public void deleteUserFromFriend(long userId1, long userId2) {

        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);

        if (!user1.getUserFriends().contains(userId2)) {
            return;
        }
        userStorage.deleteFriend(user1, user2);
        feedService.addFeed(userId1, EventType.FRIEND, Operation.REMOVE, userId2);
    }

    public List<Long> getJointFriends(long userId1, long userId2) {


        return getUserById(userId1).getUserFriends().stream()
                .filter(friendId -> getUserById(userId2).getUserFriends().contains(friendId))
                .toList();
    }

    public List<User> getFriendList(long userId) {
        return getUserById(userId).getUserFriends().stream().map(this::getUserById).toList();
    }

    public Collection<Film> getRecommendations(long userId) {

        getUserIfExists(userId);
        return filmService.getRecommendations(userId);
    }

    public void deleteUserById(long id) {
        getUserIfExists(id);
        userStorage.deleteUserById(id);
    }

    public Collection<Feed> getFeeds(long id) {
        getUserIfExists(id);
        return feedService.getFeeds(id);
    }

    private User getUserIfExists(long userId) {

        Optional<User> optionalUser = userStorage.getById(userId);
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с id + userId2 + не найден");
        }
        return optionalUser.get();
    }

    private void checkUserName(User user) {

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}