package com.example.hexcrud.api.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexcrud.api.web.dto.client.ClientResponse;
import com.example.hexcrud.api.web.dto.client.CreateClientRequest;
import com.example.hexcrud.api.web.dto.client.UpdateClientRequest;
import com.example.hexcrud.application.usecase.client.CreateClient;
import com.example.hexcrud.application.usecase.client.DeleteClient;
import com.example.hexcrud.application.usecase.client.FindClientById;
import com.example.hexcrud.application.usecase.client.ListAllClients;
import com.example.hexcrud.application.usecase.client.UpdateClient;
import com.example.hexcrud.domain.model.client.Client;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final CreateClient createClient;
    private final UpdateClient updateClient;
    private final DeleteClient deleteClient;
    private final FindClientById findClientById;
    private final ListAllClients listAllClients;

    public ClientController(CreateClient createClient, UpdateClient updateClient,
                            DeleteClient deleteClient, FindClientById findClientById,
                            ListAllClients listAllClients) {
        this.createClient = createClient;
        this.updateClient = updateClient;
        this.deleteClient = deleteClient;
        this.findClientById = findClientById;
        this.listAllClients = listAllClients;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid CreateClientRequest request) {
        var input = new CreateClient.Input(request.name(), request.email());
        Client createdClient = createClient.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromDomain(createdClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable String id, @RequestBody @Valid UpdateClientRequest request) {
        var input = new UpdateClient.Input(id, request.name(), request.email());
        Client updatedClient = updateClient.execute(input);
        return ResponseEntity.ok(ClientResponse.fromDomain(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        var input = new DeleteClient.Input(id);
        deleteClient.execute(input);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClient(@PathVariable String id) {
        Client client = findClientById.execute(id);
        return ResponseEntity.ok(ClientResponse.fromDomain(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        List<ClientResponse> clients = listAllClients.execute().stream()
                .map(ClientResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
}