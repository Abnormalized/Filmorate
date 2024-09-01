package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addUserToFriend(long userId1, long userId2) {
        if (userStorage.users.get(userId1).getUserFriends().contains(userId2)) {
            throw new NullPointerException();
        }
        UserStorage.users.get(userId1).getUserFriends().add(userId2);
        UserStorage.users.get(userId2).getUserFriends().add(userId1);
    }

    public void deleteUserFromFriend(long userId1, long userId2) {
        if (!userStorage.users.containsKey(userId2)) {
            throw new NullPointerException();
        }
        if (!userStorage.users.get(userId1).getUserFriends().contains(userId2)) {
            return;
        }
        UserStorage.users.get(userId1).getUserFriends().remove(userId2);
        UserStorage.users.get(userId2).getUserFriends().remove(userId1);
    }

    public List<Long> getJointFriends(long userId1, long userId2) {

        return UserStorage.users.get(userId1).getUserFriends().stream()
                .filter(friendId -> UserStorage.users.get(userId2).getUserFriends().contains(friendId))
                .toList();
    }
}