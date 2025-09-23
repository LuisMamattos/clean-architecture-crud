# Clean Architecture CRUD

A practical example of a CRUD application using Java & Spring Boot to demonstrate a modular, pragmatic variation of the **Hexagonal (Ports & Adapters)** and **Clean Architecture** patterns.

The application manages three core business entities: Clients, Products, and Orders, featuring a complete order lifecycle.

## Architectural Overview

This project follows a modular, Hexagonal architecture designed for high testability and separation of concerns. The core business logic is isolated within the `domain` and `application` modules, which are pure Java and framework-agnostic.

The `api` module acts as the application's entrypoint and composition root, handling web requests, while the `infrastructure` module contains database persistence implementations. All dependencies point inwards towards the `domain`, ensuring the core logic remains independent of external technologies.

## Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **Maven** (Multi-module project)
- **MongoDB** (via Spring Data MongoDB)
