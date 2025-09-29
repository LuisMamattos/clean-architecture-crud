package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

public class CreateClientImpl implements CreateClient {

    private final ClientRepository clientRepository;

    public CreateClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    // A assinatura agora corresponde à interface e retorna 'Client'
    public Client execute(Input input) {
        // A lógica de validação agora lança a exceção
        if (clientRepository.existsByEmail(input.email())) {
            throw new BusinessRuleException("Email already in use: " + input.email());
        }

        Client newClient = new Client(input.name(), input.email());
        
        // A lógica de sucesso agora retorna a entidade salva diretamente
        return clientRepository.save(newClient);
    }
}