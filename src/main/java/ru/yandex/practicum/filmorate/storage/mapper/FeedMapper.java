package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getLong("EVENT_ID"));
        feed.setTimestamp(rs.getLong("TIME"));
        feed.setOperation(Operation.valueOf(rs.getString("OPERATION")));
        feed.setEventType(EventType.valueOf(rs.getString("EVENT_TYPE")));
        feed.setEntityId(rs.getLong("ENTITY_ID"));
        feed.setUserId(rs.getLong("USER_ID"));
        return feed;
    }
}