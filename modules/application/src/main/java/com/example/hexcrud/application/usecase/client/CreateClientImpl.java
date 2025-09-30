package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class CreateClientImpl implements CreateClient {

    private final ClientRepository clientRepository;

    public CreateClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client execute(Input input) {
        if (clientRepository.findByEmail(input.email()).isPresent()) {
            throw new BusinessRuleException("Email already in use: " + input.email());
        }
        Client newClient = Client.create(input.name(), input.email());
        
        return clientRepository.save(newClient);
    }
}