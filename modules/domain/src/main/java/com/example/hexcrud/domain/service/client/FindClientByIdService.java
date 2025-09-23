package com.example.hexcrud.domain.service.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;

public interface FindClientByIdService {
    Optional<Client> execute(String id);
}