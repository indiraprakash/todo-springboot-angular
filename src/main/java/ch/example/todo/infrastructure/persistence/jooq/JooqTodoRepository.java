package ch.example.todo.infrastructure.persistence.jooq;

import ch.example.todo.application.port.TodoRepository;
import ch.example.todo.domain.model.Todo;
import ch.example.todo.domain.model.TodoId;
import ch.example.todo.infrastructure.persistence.jooq.generated.tables.records.TodoRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ch.example.todo.infrastructure.persistence.jooq.generated.Tables.TODO;

@Repository
public class JooqTodoRepository implements TodoRepository {

    private final DSLContext dsl;
    private final TodoRecordMapper mapper;

    public JooqTodoRepository(DSLContext dsl, TodoRecordMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public List<Todo> findAll() {
        return dsl.selectFrom(TODO)
                .orderBy(TODO.CREATED_AT.desc())
                .fetch(mapper::toDomain);
    }

    @Override
    public Optional<Todo> findById(TodoId id) {
        TodoRecord record = dsl.selectFrom(TODO)
                .where(TODO.ID.eq(id.value()))
                .fetchOne();
        return Optional.ofNullable(record).map(mapper::toDomain);
    }

    @Override
    public Todo insert(Todo todo) {
        dsl.insertInto(TODO)
                .set(TODO.ID, todo.id().value())
                .set(TODO.TITLE, todo.title())
                .set(TODO.DESCRIPTION, todo.description())
                .set(TODO.STATUS, todo.status().name())
                .set(TODO.CREATED_AT, todo.createdAt())
                .set(TODO.UPDATED_AT, todo.updatedAt())
                .execute();
        return todo;
    }

    @Override
    public boolean update(Todo todo) {
        return dsl.update(TODO)
                .set(TODO.TITLE, todo.title())
                .set(TODO.DESCRIPTION, todo.description())
                .set(TODO.STATUS, todo.status().name())
                .set(TODO.UPDATED_AT, todo.updatedAt())
                .where(TODO.ID.eq(todo.id().value()))
                .execute() > 0;
    }

    @Override
    public boolean deleteById(TodoId id) {
        return dsl.deleteFrom(TODO)
                .where(TODO.ID.eq(id.value()))
                .execute() > 0;
    }
}
