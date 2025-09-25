package com.example.hexcrud.infrastructure.repository;

import org.springframework.data.repository.Repository;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public interface ClientRepositoryImpl extends Repository<Client, String>, ClientRepository {
    // O Spring implementará os métodos herdados de ClientRepository.
    void deleteAll(); //Método adicional, necessário pra teste.
}