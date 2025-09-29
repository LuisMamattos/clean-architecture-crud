package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class DeleteClientImpl implements DeleteClient {

    private final ClientRepository clientRepository;

    public DeleteClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void execute(Input input) {
        
        if (!clientRepository.existsById(input.id())) {
            throw new ResourceNotFoundException("Client not found with ID: " + input.id());
        }
        //Caminho Feliz
        clientRepository.deleteById(input.id());
    }
}