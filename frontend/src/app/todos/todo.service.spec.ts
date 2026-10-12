import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Todo, TodoStatus } from './todo.model';
import { TodoService } from './todo.service';

describe('TodoService', () => {
  let service: TodoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TodoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads todos from the configured API', () => {
    const todos: Todo[] = [{
      id: 'todo-id',
      title: 'Read',
      description: '',
      status: TodoStatus.PENDING,
      createdAt: '2026-10-11T06:00:00',
      updatedAt: '2026-10-11T06:00:00',
    }];

    service.getTodos().subscribe((result) => expect(result).toEqual(todos));

    http.expectOne('http://localhost:8080/api/todos').flush(todos);
  });

  it('posts new todos with their title and description', () => {
    service.createTodo('Write tests', 'Cover the API client').subscribe();

    const request = http.expectOne('http://localhost:8080/api/todos');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      title: 'Write tests',
      description: 'Cover the API client',
    });
    request.flush({
      id: 'todo-id',
      title: 'Write tests',
      description: 'Cover the API client',
      status: TodoStatus.PENDING,
      createdAt: '2026-10-11T06:00:00',
      updatedAt: '2026-10-11T06:00:00',
    });
  });

  it('updates an existing todo', () => {
    service.updateTodo('todo-id', 'Updated task', 'New details').subscribe();

    const request = http.expectOne('http://localhost:8080/api/todos/todo-id');
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual({
      title: 'Updated task',
      description: 'New details',
    });
    request.flush({
      id: 'todo-id',
      title: 'Updated task',
      description: 'New details',
      status: TodoStatus.PENDING,
      createdAt: '2026-10-11T06:00:00',
      updatedAt: '2026-10-11T06:01:00',
    });
  });
});
