package com.example.hexcrud.infrastructure.repository.client;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;


public class ClientRepositoryImpl implements ClientRepository {

    private final ClientMongoRepository repository;

    public ClientRepositoryImpl(ClientMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client save(Client client) { return repository.save(client); }

    @Override
    public Optional<Client> findById(String id) { return repository.findById(id); }

    @Override
    public List<Client> findAll() { return repository.findAll(); }

    @Override
    public void delete(String id) { repository.deleteById(id); }

    @Override
    public Optional<Client> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}