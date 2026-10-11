package ch.example.todo.application.service;

import ch.example.todo.domain.model.TodoId;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(TodoId id) {
        super("Todo not found: " + id.value());
    }
}
