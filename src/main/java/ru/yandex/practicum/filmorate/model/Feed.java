package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feed {
    long eventId;
    long timestamp;
    EventType eventType;
    Operation operation;
    Long entityId;
    Long userId;
}