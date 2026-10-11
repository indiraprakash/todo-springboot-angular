package ch.example.todo.application.service;

import ch.example.todo.application.port.TodoRepository;
import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;
import ch.example.todo.domain.model.TodoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository repository;
    private final Clock clock;

    @Autowired
    public TodoService(TodoRepository repository) {
        this(repository, Clock.systemDefaultZone());
    }

    TodoService(TodoRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public List<Todo> findAll() {
        return repository.findAll();
    }

    public Todo findById(TodoId id) {
        return repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    public Todo create(String title, String description) {
        LocalDateTime now = LocalDateTime.now(clock);
        return repository.insert(new Todo(
                TodoId.generate(), title, description, TodoStatus.PENDING, now, now));
    }

    public Todo update(TodoId id, String title, String description) {
        Todo current = findById(id);
        Todo updated = current.withDetails(title, description, LocalDateTime.now(clock));
        repository.update(updated);
        return updated;
    }

    public Todo updateStatus(TodoId id, TodoStatus status) {
        Todo current = findById(id);
        Todo updated = current.withStatus(status, LocalDateTime.now(clock));
        repository.update(updated);
        return updated;
    }

    public void delete(TodoId id) {
        if (!repository.deleteById(id)) {
            throw new TodoNotFoundException(id);
        }
    }
}
