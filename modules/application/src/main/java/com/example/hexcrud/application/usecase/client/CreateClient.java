package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;

public interface CreateClient {
    record Input(String name, String email) {}   
    Client execute(Input input);
}