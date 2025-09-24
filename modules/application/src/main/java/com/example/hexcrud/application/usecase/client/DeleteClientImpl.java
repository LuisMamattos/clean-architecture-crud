package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.repository.client.ClientRepository;

public class DeleteClientImpl implements DeleteClient {

    private final ClientRepository clientRepository;

    public DeleteClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Output execute(Input input) {
        return clientRepository.findById(input.id())
                .map(client -> {
                    clientRepository.delete(input.id());
                    return (Output) new Output.Deleted();
                })
                .orElse(new Output.NotFound(input.id()));
    }
}