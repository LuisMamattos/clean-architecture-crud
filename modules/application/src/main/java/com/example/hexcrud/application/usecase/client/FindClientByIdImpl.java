package com.example.hexcrud.application.usecase.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class FindClientByIdImpl implements FindClientById {

    private final ClientRepository clientRepository;

    public FindClientByIdImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> execute(String id) {
        return clientRepository.findById(id);
    }
}