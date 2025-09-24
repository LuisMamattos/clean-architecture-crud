package com.example.hexcrud.application.usecase.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class UpdateClientImpl implements UpdateClient {

    private final ClientRepository clientRepository;

    public UpdateClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Output execute(Input input) {
        Optional<Client> optionalClient = clientRepository.findById(input.id());
        if (optionalClient.isEmpty()) {
            return new Output.NotFound(input.id());
        }

        Client clientToUpdate = optionalClient.get();

        if (!clientToUpdate.getEmail().equalsIgnoreCase(input.newEmail())) {
            Optional<Client> existingClientWithNewEmail = clientRepository.findByEmail(input.newEmail());
            if (existingClientWithNewEmail.isPresent()) {
                return new Output.EmailAlreadyExists(input.newEmail());
            }
        }

        clientToUpdate.updateDetails(input.newName(), input.newEmail());
        Client updatedClient = clientRepository.save(clientToUpdate);

        return new Output.Updated(updatedClient);
    }
}