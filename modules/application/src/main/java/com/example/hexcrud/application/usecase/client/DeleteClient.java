package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.port.in.client.DeleteClientUseCase;
import com.example.hexcrud.domain.port.out.client.ClientRepositoryPort;

public class DeleteClient implements DeleteClientUseCase {

    private final ClientRepositoryPort clientRepository;

    public DeleteClient(ClientRepositoryPort clientRepository) {
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