import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Todo, TodoStatus } from './todo.model';

@Component({
  selector: 'app-todo-item',
  standalone: true,
  templateUrl: './todo-item.component.html',
  styleUrl: './todo-item.component.css',
})
export class TodoItemComponent {
  @Input({ required: true }) todo!: Todo;
  @Output() statusChange = new EventEmitter<TodoStatus>();
  @Output() remove = new EventEmitter<void>();

  readonly TodoStatus = TodoStatus;

  onStatusChange(event: Event): void {
    const completed = (event.target as HTMLInputElement).checked;
    this.statusChange.emit(completed ? TodoStatus.COMPLETED : TodoStatus.PENDING);
  }
}
