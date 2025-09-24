package com.example.hexcrud.application.usecase.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;

public interface FindClientById {
    Optional<Client> execute(String id);
}