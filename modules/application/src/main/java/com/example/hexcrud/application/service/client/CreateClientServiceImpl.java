package com.example.hexcrud.application.service.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.service.client.CreateClientService;

public class CreateClientServiceImpl implements CreateClientService {

    private final ClientRepositoryPort clientRepository;

    public CreateClientServiceImpl(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Output execute(Input input) {
        Optional<Client> existingClient = clientRepository.findByEmail(input.email());
        if (existingClient.isPresent()) {
            return new Output.EmailAlreadyExists(input.email());
        }

        Client newClient = new Client(input.name(), input.email());
        Client savedClient = clientRepository.save(newClient);

        return new Output.Created(savedClient);
    }
}