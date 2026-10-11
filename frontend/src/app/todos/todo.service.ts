import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Todo, TodoStatus } from './todo.model';

@Injectable({ providedIn: 'root' })
export class TodoService {
  private readonly http = inject(HttpClient);
  private readonly endpoint = `${environment.apiUrl}/todos`;

  getTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.endpoint);
  }

  createTodo(title: string, description: string): Observable<Todo> {
    return this.http.post<Todo>(this.endpoint, { title, description });
  }

  updateStatus(id: string, status: TodoStatus): Observable<Todo> {
    return this.http.patch<Todo>(`${this.endpoint}/${id}/status`, { status });
  }

  deleteTodo(id: string): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }
}
