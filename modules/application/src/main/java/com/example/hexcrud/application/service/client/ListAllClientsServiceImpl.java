package com.example.hexcrud.application.service.client;

import java.util.List;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.service.client.ListAllClientsService;

public class ListAllClientsServiceImpl implements ListAllClientsService {

    private final ClientRepositoryPort clientRepository;

    public ListAllClientsServiceImpl(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> execute() {
        return clientRepository.findAll(); 
    }
}