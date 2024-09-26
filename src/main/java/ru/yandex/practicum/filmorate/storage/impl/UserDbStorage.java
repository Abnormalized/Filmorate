package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.*;

@Component
@AllArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getById(long id) {
        Integer res = jdbcTemplate.queryForObject(
                        "SELECT COUNT(user_id) AS sum FROM users WHERE user_id = ?",
                        (rs, rowNum) -> rs.getInt("sum"), id);
        if (res == null || res == 0) {
            throw new NoSuchElementException();
        }
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new UserMapper(), id);

        assert user != null;
        user.setUserFriends(getAllFriends(user));
        user.setLikedFilms(getLikedFilms(user));
        return user;
    }

    @Override
    public User create(User user) {
        String sqlQuery = """
                INSERT INTO users(email, login, first_name, birthday)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE login = ? AND email = ?",
                new UserMapper(), user.getLogin(), user.getEmail());
    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public User update(User userNewInfo) {
        String sqlQuery = """
                UPDATE users SET
                email = ?,
                login = ?,
                first_name = ?,
                birthday = ?
                WHERE user_id = ?
                """;

        getById(userNewInfo.getId());

        jdbcTemplate.update(sqlQuery,
                userNewInfo.getEmail(),
                userNewInfo.getLogin(),
                userNewInfo.getName(),
                userNewInfo.getBirthday(),
                userNewInfo.getId());

        return userNewInfo;
    }

    @Override
    public Set<Long> getAllFriends(User user) {
        Set<Long> friendsSet = new HashSet<>(getAcceptedFriends(user.getId()));
        friendsSet.addAll(getAskedUsers(user.getId()));
        return friendsSet;
    }

    // Возвращает только тех друзей, которые сами отправили запрос в друзья
    @Override
    public Set<Long> getAcceptedFriends(long userId) {
        final String query = """
        SELECT requester_id
        FROM friend_users
        WHERE responser_id = ? AND accepted = true
        """;

        return new HashSet<>(jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("requester_id"), userId));
    }

    // Возвращает только тех друзей, которые приняли (или могут, но еще не приняли)
    // запрос в друзья от этого пользователя
    @Override
    public Set<Long> getAskedFriends(long userId) {
        final String query = """
        SELECT responser_id
        FROM friend_users
        WHERE requester_id = ?
        """;

        return new HashSet<>(jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("responser_id"), userId));
    }

    // Возвращает только тех пользователей, которые еще не приняли запрос в друзья
    @Override
    public Set<Long> getAskedUsers(long userId) {
        final String query = """
        SELECT responser_id
        FROM friend_users
        WHERE requester_id = ?
        """;

        return new HashSet<>(jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("responser_id"), userId));
    }

    @Override
    public boolean addFriend(User user, User friend) {
        final String query = """
                INSERT INTO friend_users (requester_id, responser_id, accepted)
                VALUES(?, ?, ?)
                """;

        jdbcTemplate.update(query,
                user.getId(),
                friend.getId(),
                false);

        return true;
    }

    @Override
    public boolean acceptFriend(User user, User friend) {
        final String query = """
                UPDATE friend_users SET
                accepted = true
                WHERE responser_id = ? AND requester_id = ? AND accepted = false
                """;

        jdbcTemplate.update(query,
                user.getId(),
                friend.getId());
        return true;
    }

    @Override
    public boolean deleteFriend(User user, User friend) {
        String sqlQuery = """
                    DELETE FROM friend_users
                    WHERE requester_id = ? AND responser_id = ?
                    OR responser_id = ? AND requester_id = ?
                    """;
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId(), user.getId(), friend.getId());
        return true;
    }

    @Override
    public Set<Long> getLikedFilms(User user) {
        final String query = """
        SELECT user_id,
               film_id
        FROM user_liked_films
        WHERE user_id = ?
        """;

        return new HashSet<>(jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("film_id"), user.getId()));
    }
}