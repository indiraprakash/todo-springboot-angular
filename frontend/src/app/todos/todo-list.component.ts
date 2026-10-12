import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoItemComponent } from './todo-item.component';
import { Todo, TodoStatus } from './todo.model';
import { TodoService } from './todo.service';

@Component({
  selector: 'app-todo-list',
  standalone: true,
  imports: [FormsModule, TodoItemComponent],
  templateUrl: './todo-list.component.html',
  styleUrl: './todo-list.component.css',
})
export class TodoListComponent implements OnInit {
  readonly todos = signal<Todo[]>([]);
  readonly title = signal('');
  readonly description = signal('');
  readonly error = signal('');
  readonly loading = signal(true);
  readonly TodoStatus = TodoStatus;

  constructor(private readonly todoService: TodoService) {}

  ngOnInit(): void {
    this.loadTodos();
  }

  addTodo(): void {
    const title = this.title().trim();
    if (!title) {
      return;
    }

    this.error.set('');
    this.todoService.createTodo(title, this.description().trim()).subscribe({
      next: (todo) => {
        this.todos.update((todos) => [todo, ...todos]);
        this.title.set('');
        this.description.set('');
      },
      error: () => this.error.set('Could not add this task. Please try again.'),
    });
  }

  changeStatus(todo: Todo, status: TodoStatus): void {
    this.todoService.updateStatus(todo.id, status).subscribe({
      next: (updated) => this.replaceTodo(updated),
      error: () => this.error.set('Could not update this task. Please try again.'),
    });
  }

  removeTodo(todo: Todo): void {
    this.todoService.deleteTodo(todo.id).subscribe({
      next: () => this.todos.update((todos) => todos.filter((item) => item.id !== todo.id)),
      error: () => this.error.set('Could not delete this task. Please try again.'),
    });
  }

  private loadTodos(): void {
    this.todoService.getTodos().subscribe({
      next: (todos) => {
        this.todos.set(todos);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load your tasks. Check that the API is running.');
        this.loading.set(false);
      },
    });
  }

  private replaceTodo(updated: Todo): void {
    this.todos.update((todos) => todos.map((todo) => todo.id === updated.id ? updated : todo));
  }
}
