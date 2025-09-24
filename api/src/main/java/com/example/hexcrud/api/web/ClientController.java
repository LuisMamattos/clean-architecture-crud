package com.example.hexcrud.api.web;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequest request) {
        var input = new CreateClient.Input(request.name(), request.email());
        var result = createClient.execute(input);

        return switch (result) {
            case CreateClient.Output.Created res ->
                    ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromDomain(res.client()));
            case CreateClient.Output.EmailAlreadyExists err ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable String id, @RequestBody UpdateClientRequest request) {
        var input = new UpdateClient.Input(id, request.name(), request.email());
        var result = updateClient.execute(input);

        return switch (result) {
            case UpdateClient.Output.Updated res ->
                    ResponseEntity.ok(ClientResponse.fromDomain(res.client()));
            case UpdateClient.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
            case UpdateClient.Output.EmailAlreadyExists err ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        var input = new DeleteClient.Input(id);
        var result = deleteClient.execute(input);

        return switch (result) {
            case DeleteClient.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteClient.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClient(@PathVariable String id) {
        return findClientById.execute(id)
                .map(ClientResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        List<ClientResponse> clients = listAllClients.execute().stream()
                .map(ClientResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
}