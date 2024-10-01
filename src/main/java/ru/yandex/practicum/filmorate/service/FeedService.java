package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedService {

    final FeedStorage feedStorage;

    public Collection<Feed> getFeeds(long id) {
        return feedStorage.getFeeds(id);
    }

    public void addFeed(Long userId, EventType eventType, Operation operation, Long entityId) {
        try {
            feedStorage.addFeed(userId, eventType, operation, entityId);
        } catch (SQLException e) {
            log.error("Feed record - {}", e.getMessage());
        }
    }
}
