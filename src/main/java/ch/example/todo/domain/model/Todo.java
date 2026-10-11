package ch.example.todo.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record Todo(
        TodoId id,
        String title,
        String description,
        TodoStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public Todo {
        Objects.requireNonNull(id, "Todo ID cannot be null");
        Objects.requireNonNull(title, "Todo title cannot be null");
        title = title.trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Todo title cannot be blank");
        }
        description = description == null ? "" : description.trim();
        Objects.requireNonNull(status, "Todo status cannot be null");
        Objects.requireNonNull(createdAt, "Creation time cannot be null");
        Objects.requireNonNull(updatedAt, "Update time cannot be null");
    }

    public Todo withDetails(String title, String description, LocalDateTime updatedAt) {
        return new Todo(id, title, description, status, createdAt, updatedAt);
    }

    public Todo withStatus(TodoStatus status, LocalDateTime updatedAt) {
        return new Todo(id, title, description, status, createdAt, updatedAt);
    }
}
