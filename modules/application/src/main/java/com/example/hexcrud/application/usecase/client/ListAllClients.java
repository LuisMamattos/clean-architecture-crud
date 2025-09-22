package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.port.in.client.ListAllClientsUseCase;
import com.example.hexcrud.domain.port.out.client.ClientRepositoryPort;
import java.util.List;

public class ListAllClients implements ListAllClientsUseCase {

    private final ClientRepositoryPort clientRepository;

    public ListAllClients(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> execute() {
        return clientRepository.findAll(); 
    }
}