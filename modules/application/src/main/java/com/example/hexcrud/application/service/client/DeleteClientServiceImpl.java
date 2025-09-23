package com.example.hexcrud.application.service.client;

import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.service.client.DeleteClientService;

public class DeleteClientServiceImpl implements DeleteClientService {

    private final ClientRepositoryPort clientRepository;

    public DeleteClientServiceImpl(ClientRepositoryPort clientRepository) {
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