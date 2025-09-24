package com.example.hexcrud.application.usecase.client;

import java.util.List;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class ListAllClientsImpl implements ListAllClients {

    private final ClientRepository clientRepository;

    public ListAllClientsImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> execute() {
        return clientRepository.findAll(); 
    }
}