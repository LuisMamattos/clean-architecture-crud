package com.example.hexcrud.domain.repository.client;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.client.Client;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(String id);
    List<Client> findAll();
    void deleteById(String id);
    Optional<Client> findByEmail(String email);
    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, String id);
}
