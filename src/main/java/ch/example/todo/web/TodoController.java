package ch.example.todo.web;

import ch.example.todo.application.service.TodoNotFoundException;
import ch.example.todo.application.service.TodoService;
import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;
import ch.example.todo.domain.model.TodoStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TodoResponse> findAll() {
        return service.findAll().stream().map(TodoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TodoResponse findById(@PathVariable UUID id) {
        return TodoResponse.from(service.findById(new TodoId(id)));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody CreateTodoRequest request) {
        Todo todo = service.create(request.title(), request.description());
        return ResponseEntity.created(URI.create("/api/todos/" + todo.id().value()))
                .body(TodoResponse.from(todo));
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateTodoRequest request) {
        return TodoResponse.from(service.update(new TodoId(id), request.title(), request.description()));
    }

    @PatchMapping("/{id}/status")
    public TodoResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest request) {
        return TodoResponse.from(service.updateStatus(new TodoId(id), request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(new TodoId(id));
        return ResponseEntity.noContent().build();
    }

    public record CreateTodoRequest(
            @NotBlank @Size(max = 200) String title,
            @Size(max = 2000) String description) {
    }

    public record UpdateTodoRequest(
            @NotBlank @Size(max = 200) String title,
            @Size(max = 2000) String description) {
    }

    public record UpdateStatusRequest(@NotNull TodoStatus status) {
    }

    public record TodoResponse(
            UUID id,
            String title,
            String description,
            TodoStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        private static TodoResponse from(Todo todo) {
            return new TodoResponse(todo.id().value(), todo.title(), todo.description(),
                    todo.status(), todo.createdAt(), todo.updatedAt());
        }
    }
}

@RestControllerAdvice
class TodoExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    ResponseEntity<ErrorResponse> handleNotFound(TodoNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    record ErrorResponse(String message) {
    }
}
