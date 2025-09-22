# Clean Architecture CRUD

This project is a practical example of a CRUD application built with Java and Spring Boot. Its primary goal is to demonstrate and explore a pragmatic, modular variation of the **Hexagonal (Ports & Adapters)** and **Clean Architecture** patterns.

The application manages three core business entities: Clients, Products, and Orders, featuring a complete order lifecycle from creation to confirmation.

## Architectural Principles

The architecture is designed to be robust, scalable, and highly testable by strictly separating concerns into distinct Maven modules. It follows a "freestyle" Hexagonal approach that emphasizes a pure business core and a dedicated composition root.

The key principle is the **Dependency Rule**: all dependencies point inwards, towards the `domain`.

### Module Structure

- **`api`**: The Composition Root and the only module aware of the Spring Boot framework. It's responsible for:

  - Running the application (`main` class).
  - Exposing REST endpoints (driving adapters).
  - Wiring the application together using `@Configuration` and `@Bean` definitions, connecting use case implementations with their infrastructure dependencies.

- **`application`**: The application layer, containing the orchestration logic.

  - It holds the concrete implementations of the use cases (e.g., `CreateOrder.java`).
  - **Crucially, this module is framework-agnostic.** It's a pure Java module with no Spring dependencies.

- **`domain`**: The core of the business logic.

  - Contains the rich domain models with their business rules and invariants (e.g., `Order.java`).
  - Defines the contracts (**Ports**) for all interactions with the core, such as `UseCase` interfaces (input ports) and `RepositoryPort` interfaces (output ports).
  - This is a pure Java module.

- **`infrastructure`**: The implementation of external concerns.
  - Contains the concrete **Adapters** for output ports defined in the `domain`.
  - This includes repository implementations that talk to a database (e.g., `OrderMongoRepositoryAdapter.java`).

### Benefits of this "Freestyle" Architecture

This approach maintains the core benefits of a pure Hexagonal Architecture while providing a clear separation for the application's entrypoint:

1.  **High Testability:** The business core (`domain` + `application`) can be unit-tested in complete isolation, without needing a running database or web server.
2.  **Technology Independence:** The core logic has no knowledge of the database (MongoDB) or the web framework (Spring Web). The `infrastructure` module can be swapped with a different implementation (e.g., for a SQL database) without any changes to the `domain` or `application` layers.
3.  **Clear Separation of Concerns:** Each module has a single, well-defined responsibility, making the codebase easier to understand, maintain, and scale.

## Technologies Used

- **Java 21**
- **Spring Boot 3.x**
- **Maven** (Multi-module project)
- **MongoDB** (via Spring Data MongoDB)
