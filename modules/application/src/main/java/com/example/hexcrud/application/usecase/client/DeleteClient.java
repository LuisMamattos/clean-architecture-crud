package com.example.hexcrud.application.usecase.client;

public interface DeleteClient {
    
    record Input(String id) {}

    sealed interface Output {
        record Deleted() implements Output {}
        record NotFound(String id) implements Output {}
    }

    Output execute(Input input);
}
