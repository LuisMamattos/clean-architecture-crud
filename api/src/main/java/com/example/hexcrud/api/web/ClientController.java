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
import com.example.hexcrud.domain.port.in.client.CreateClientUseCase;
import com.example.hexcrud.domain.port.in.client.DeleteClientUseCase;
import com.example.hexcrud.domain.port.in.client.FindClientByIdUseCase;
import com.example.hexcrud.domain.port.in.client.ListAllClientsUseCase;
import com.example.hexcrud.domain.port.in.client.UpdateClientUseCase;

@RestController
@RequestMapping("/clients")
public class ClientController {

    // --- MUDANÇA: Injetar as INTERFACES, não as classes concretas ---
    private final CreateClientUseCase createClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final FindClientByIdUseCase findClientByIdUseCase;
    private final ListAllClientsUseCase listAllClientsUseCase;

    public ClientController(CreateClientUseCase createClientUseCase, UpdateClientUseCase updateClientUseCase,
                            DeleteClientUseCase deleteClientUseCase, FindClientByIdUseCase findClientByIdUseCase,
                            ListAllClientsUseCase listAllClientsUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.findClientByIdUseCase = findClientByIdUseCase;
        this.listAllClientsUseCase = listAllClientsUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequest request) {
        // Mapeia DTO para o Input do caso de uso
        var input = new CreateClientUseCase.Input(request.name(), request.email());
        var result = createClientUseCase.execute(input);

        return switch (result) {
            // Mapeia Output do caso de uso para a Response da API (usando DTO)
            case CreateClientUseCase.Output.Created res -> 
                ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromDomain(res.client()));
            case CreateClientUseCase.Output.EmailAlreadyExists err -> 
                ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable String id, @RequestBody UpdateClientRequest request) {
        // Mapeia DTO para o Input do caso de uso
        var input = new UpdateClientUseCase.Input(id, request.name(), request.email());
        var result = updateClientUseCase.execute(input);

        return switch (result) {
            // Mapeia Output para a Response (DTO)
            case UpdateClientUseCase.Output.Updated res -> 
                ResponseEntity.ok(ClientResponse.fromDomain(res.client()));
            case UpdateClientUseCase.Output.NotFound err -> 
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
            case UpdateClientUseCase.Output.EmailAlreadyExists err -> 
                ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        var input = new DeleteClientUseCase.Input(id);
        var result = deleteClientUseCase.execute(input);

        return switch (result) {
            case DeleteClientUseCase.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteClientUseCase.Output.NotFound err -> 
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClient(@PathVariable String id) {
        return findClientByIdUseCase.execute(id)
                // Mapeia o modelo de domínio para o DTO de resposta
                .map(ClientResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        List<ClientResponse> clients = listAllClientsUseCase.execute().stream()
                // Mapeia a lista de modelos de domínio para uma lista de DTOs
                .map(ClientResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
}