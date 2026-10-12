package ch.example.todo.domain.model;

import java.util.Objects;
import java.util.UUID;

public record TodoId(UUID value) {

    public TodoId {
        Objects.requireNonNull(value, "Todo ID cannot be null");
    }

    public static TodoId generate() {
        return new TodoId(UUID.randomUUID());
    }
}
