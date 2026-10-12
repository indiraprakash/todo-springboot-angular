package ch.example.todo.application.service;

import ch.example.todo.application.port.TodoRepository;
import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;
import ch.example.todo.domain.model.TodoStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TodoServiceTest {

    private static final Instant NOW = Instant.parse("2026-10-11T06:00:00Z");

    private final InMemoryTodoRepository repository = new InMemoryTodoRepository();
    private final TodoService service = new TodoService(
            repository, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void createsPendingTodoWithCurrentTimestamps() {
        Todo todo = service.create("  Review PR  ", "  Check API  ");

        assertEquals("Review PR", todo.title());
        assertEquals("Check API", todo.description());
        assertEquals(TodoStatus.PENDING, todo.status());
        assertEquals(NOW, todo.createdAt().toInstant(ZoneOffset.UTC));
        assertEquals(todo, repository.findById(todo.id()).orElseThrow());
    }

    @Test
    void updatesTodoWithoutChangingItsCreationTime() {
        Todo created = service.create("Old title", "");

        Todo updated = service.update(created.id(), "New title", "Details");

        assertEquals("New title", updated.title());
        assertEquals(created.createdAt(), updated.createdAt());
        assertEquals("Details", service.findById(created.id()).description());
    }

    @Test
    void changesStatusAndRejectsMissingTodo() {
        Todo created = service.create("Task", null);

        assertEquals(TodoStatus.COMPLETED,
                service.updateStatus(created.id(), TodoStatus.COMPLETED).status());
        assertThrows(TodoNotFoundException.class,
                () -> service.findById(new TodoId(java.util.UUID.randomUUID())));
    }

    private static class InMemoryTodoRepository implements TodoRepository {

        private final List<Todo> todos = new ArrayList<>();

        @Override
        public List<Todo> findAll() {
            return List.copyOf(todos);
        }

        @Override
        public Optional<Todo> findById(TodoId id) {
            return todos.stream().filter(todo -> todo.id().equals(id)).findFirst();
        }

        @Override
        public Todo insert(Todo todo) {
            todos.add(todo);
            return todo;
        }

        @Override
        public boolean update(Todo todo) {
            int index = todos.indexOf(findById(todo.id()).orElse(null));
            if (index < 0) {
                return false;
            }
            todos.set(index, todo);
            return true;
        }

        @Override
        public boolean deleteById(TodoId id) {
            return todos.removeIf(todo -> todo.id().equals(id));
        }
    }
}
