package com.example.hexcrud.domain.service.client;

import java.util.List;

import com.example.hexcrud.domain.model.client.Client;

public interface ListAllClientsService {
    List<Client> execute();
}