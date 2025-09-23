package com.example.hexcrud.application.service.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.service.client.FindClientByIdService;

public class FindClientByIdServiceImpl implements FindClientByIdService {

    private final ClientRepositoryPort clientRepository;

    public FindClientByIdServiceImpl(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> execute(String id) {
        return clientRepository.findById(id);
    }
}