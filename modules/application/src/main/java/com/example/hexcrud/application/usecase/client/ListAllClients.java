package com.example.hexcrud.application.usecase.client;

import java.util.List;

import com.example.hexcrud.domain.model.client.Client;

public interface ListAllClients {
    List<Client> execute();
}