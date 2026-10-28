# SYNTH // Procedural City Engine

A deterministic procedural city generation REST API built with Kotlin and Spring Boot.

SYNTH generates configurable 2D city layouts with roads, residential buildings, commercial centers, and industrial districts. Generated cities can be stored in PostgreSQL and retrieved through a REST API.

## Features

- Deterministic generation using a seed
- Configurable road spacing
- Configurable commercial center radius
- Seed-based industrial district placement and configurable district size
- City creation, listing, retrieval, and deletion
- PostgreSQL persistence with JSONB
- Database migrations with Flyway
- Request validation and structured API errors
- Interactive OpenAPI documentation with Swagger UI
- Automated tests using JUnit and Bruno
- Docker Compose deployment

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Backend implementation |
| Spring Boot 4 | REST API and application framework |
| Java 21 | JVM runtime |
| Spring Data JPA | Database access |
| PostgreSQL 17 | Persistent storage |
| Flyway | Database migrations |
| Gradle Kotlin DSL | Build automation |
| JUnit 5 | Automated testing |
| Bruno | API testing |
| Springdoc OpenAPI | API documentation |
| Docker Compose | Containerized deployment |

## Quick Start

Requirements: Docker and Docker Compose.

Clone the repository:

```bash
git clone https://github.com/vazovsky17/synth.git
cd synth
```

Start the application:

```bash
docker compose up --build -d
```

The API will be available at:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI specification: http://localhost:8080/v3/api-docs
- REST API: http://localhost:8080/api/v1/cities

The Docker Compose configuration uses development-only database credentials and binds exposed ports to localhost. Do not use these credentials for a public deployment.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/cities/generate` | Generate a city without saving it |
| POST | `/api/v1/cities` | Generate and save a city |
| GET | `/api/v1/cities` | List saved cities |
| GET | `/api/v1/cities/{id}` | Retrieve a saved city |
| DELETE | `/api/v1/cities/{id}` | Delete a saved city |

### Example: Generate a City

```http
GET /api/v1/cities/generate?width=30&height=30&seed=42&roadSpacing=6&industrialZoneSize=35&commercialRadius=5
```

### Example: Save a City

```http
POST /api/v1/cities
Content-Type: application/json
```

```json
{
  "name": "Synth Metropolis",
  "width": 30,
  "height": 30,
  "seed": 42,
  "roadSpacing": 6,
  "industrialZoneSize": 35,
  "commercialRadius": 5
}
```

A successful request returns HTTP `201 Created` and the saved city's ID.

## Generation Rules

City layouts are generated on a two-dimensional grid.

1. Roads are placed according to the configured spacing.
2. A commercial district is generated around the city center.
3. An industrial district is placed in a seed-selected corner.
4. Remaining cells are assigned building types using a seeded random generator.

Roads take priority over other zones.

For identical dimensions, seed, and configuration, the generator produces the same layout.

## Running Tests

With PostgreSQL running through Docker Compose:

```bash
./gradlew test
```

The Bruno API collection is located in `bruno/SYNTH API`.

## Project Status

**MVP 1.0**

The project demonstrates Kotlin backend development, REST API design, persistence, database migrations, configurable procedural generation, automated testing, and containerization.

## License

No license is specified yet.
