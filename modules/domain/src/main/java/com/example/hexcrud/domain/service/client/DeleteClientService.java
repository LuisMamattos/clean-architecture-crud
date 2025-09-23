package com.example.hexcrud.domain.service.client;

public interface DeleteClientService {
    
    record Input(String id) {}

    sealed interface Output {
        record Deleted() implements Output {}
        record NotFound(String id) implements Output {}
    }

    Output execute(Input input);
}
