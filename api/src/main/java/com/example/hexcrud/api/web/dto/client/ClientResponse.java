package com.example.hexcrud.api.web.dto.client;

import com.example.hexcrud.domain.model.client.Client;

public record ClientResponse(String id, String name, String email) {
    public static ClientResponse fromDomain(Client client) {
        return new ClientResponse(client.getId(), client.getName(), client.getEmail());
    }
}