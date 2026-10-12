package ch.example.todo.infrastructure.persistence.jooq;

import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;
import ch.example.todo.domain.model.TodoStatus;
import ch.example.todo.infrastructure.persistence.jooq.generated.tables.records.TodoRecord;
import org.springframework.stereotype.Component;

@Component
public class TodoRecordMapper {

    public Todo toDomain(TodoRecord record) {
        return new Todo(
                new TodoId(record.getId()),
                record.getTitle(),
                record.getDescription(),
                TodoStatus.valueOf(record.getStatus()),
                record.getCreatedAt(),
                record.getUpdatedAt());
    }
}
