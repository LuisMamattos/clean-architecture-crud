package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;

public interface UpdateClient {
    
    record Input(String id, String newName, String newEmail) {}

    sealed interface Output {
        record Updated(Client client) implements Output {}
        record NotFound(String id) implements Output {}
        record EmailAlreadyExists(String email) implements Output {}
    }

    Output execute(Input input);
}
