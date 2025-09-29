package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;
// O import do Optional não é mais necessário para esta lógica específica.

public class UpdateClientImpl implements UpdateClient {

    private final ClientRepository clientRepository;

    public UpdateClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client execute(Input input) {
        Client clientToUpdate = clientRepository.findById(input.id())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + input.id()));

        if (clientRepository.existsByEmailAndIdNot(input.email(), clientToUpdate.getId())) {
            throw new BusinessRuleException("Email already in use by another client: " + input.email());
        }

        clientToUpdate.updateDetails(input.name(), input.email());
        
        return clientRepository.save(clientToUpdate);
    }
}