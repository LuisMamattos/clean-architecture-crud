package com.example.hexcrud.infrastructure.repository.client;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.hexcrud.domain.model.client.Client;

public interface ClientMongoRepository extends MongoRepository<Client, String> {
    Optional<Client> findByEmail(String email);
}