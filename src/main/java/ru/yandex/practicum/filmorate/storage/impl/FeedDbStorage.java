package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.SQLException;
import java.util.Collection;

@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedDbStorage implements FeedStorage {
    final JdbcTemplate jdbcTemplate;
    final RowMapper<Feed> rowMapper;

    private static final String GET_FEEDS = """
            SELECT EVENTS.*
            FROM EVENTS
            WHERE USER_ID = ?
            ORDER BY TIME
            """;
    private static final String INSERT_FEED = """        
            INSERT INTO EVENTS(TIME, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID)
            VALUES (?, ?, ?, ?, ?)
            """;

    @Override
    public Collection<Feed> getFeeds(long id) {
        return jdbcTemplate.query(GET_FEEDS, rowMapper, id);
    }

    @Override
    public Feed addFeed(Long userId, EventType eventType, Operation operation, Long entityId) throws SQLException {

        long currentTime = System.currentTimeMillis();

        int matches = jdbcTemplate.update(INSERT_FEED,
                currentTime,
                userId,
                eventType.toString(),
                operation.toString(),
                entityId);

        if (matches == 0) {
            throw new SQLException("Не удалось добавить событиею Пользователь: " + userId
                    + " время: " + currentTime + " тип: " + eventType);
        }

        String returnSqlQuery = """
                SELECT * FROM EVENTS
                WHERE USER_ID = ?
                ORDER BY EVENT_ID DESC
                LIMIT(1)""";

        return jdbcTemplate.queryForObject(returnSqlQuery, rowMapper, userId);
    }
}
