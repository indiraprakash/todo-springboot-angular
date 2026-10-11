package ch.example.todo.application.port;

import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    List<Todo> findAll();

    Optional<Todo> findById(TodoId id);

    Todo insert(Todo todo);

    boolean update(Todo todo);

    boolean deleteById(TodoId id);
}
