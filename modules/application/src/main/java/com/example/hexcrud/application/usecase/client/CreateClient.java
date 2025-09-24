package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;

// Interface pura, sem anotações de framework.
public interface CreateClient {
    
    // Contrato de entrada
    record Input(String name, String email) {}

    // Contrato de saída
    sealed interface Output {
        record Created(Client client) implements Output {}
        record EmailAlreadyExists(String email) implements Output {}
    }

    Output execute(Input input);
}