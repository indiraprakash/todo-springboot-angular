package ch.example.todo.domain.model;

public sealed interface TodoMutation permits TodoMutation.SetDetails, TodoMutation.SetStatus {

    record SetDetails(String title, String description) implements TodoMutation {
    }

    record SetStatus(TodoStatus status) implements TodoMutation {
    }
}
