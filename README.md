# Todo

A full-stack todo application with a Spring Boot 4 / Java 25 REST API and an
Angular 21 LTS frontend. Todos are stored in an H2 in-memory database and
accessed through jOOQ.

## Requirements

- JDK 25
- Maven 3.9+
- Node.js 20.19+ or 22.12+ and npm

## Run locally

Start the backend from the repository root:

```sh
mvn spring-boot:run
```

The API runs at `http://localhost:8080` and the H2 database is created in
memory. jOOQ generates its typed table classes from
`src/main/resources/schema.sql` during the Maven build.

In another terminal, start the frontend:

```sh
cd frontend
npm ci
npm start
```

Open `http://localhost:4200`. The frontend calls the API at
`http://localhost:8080/api`.

## API

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/todos` | List todos |
| `GET` | `/api/todos/{id}` | Get one todo |
| `POST` | `/api/todos` | Create a pending todo (`title`, optional `description`) |
| `PUT` | `/api/todos/{id}` | Update title and description |
| `PATCH` | `/api/todos/{id}/status` | Set status to `PENDING` or `COMPLETED` |
| `DELETE` | `/api/todos/{id}` | Delete a todo |

## Tests and production build

```sh
mvn test
cd frontend
npm test
npm run build
```
