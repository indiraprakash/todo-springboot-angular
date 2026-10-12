export enum TodoStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
}

export interface Todo {
  id: string;
  title: string;
  description: string;
  status: TodoStatus;
  createdAt: string;
  updatedAt: string;
}
