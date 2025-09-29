package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;

public interface UpdateClient {
    record Input(String id, String name, String email) {}
    Client execute(Input input);
}