package com.example.hexcrud.application.service.client;

import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.service.client.UpdateClientService;

public class UpdateClientServiceImpl implements UpdateClientService {

    private final ClientRepositoryPort clientRepository;

    public UpdateClientServiceImpl(ClientRepositoryPort clientRepository) {
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