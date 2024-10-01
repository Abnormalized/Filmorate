package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.SQLException;
import java.util.Collection;

public interface FeedStorage {

    Collection<Feed> getFeeds(long id);

    void addFeed(Long userId, EventType eventType, Operation operation, Long entityId) throws SQLException;
}
