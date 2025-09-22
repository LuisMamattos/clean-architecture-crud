package com.example.hexcrud.application.usecase.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.port.in.client.FindClientByIdUseCase;
import com.example.hexcrud.domain.port.out.client.ClientRepositoryPort;

public class FindClientById implements FindClientByIdUseCase {

    private final ClientRepositoryPort clientRepository;

    public FindClientById(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> execute(String id) {
        return clientRepository.findById(id);
    }
}