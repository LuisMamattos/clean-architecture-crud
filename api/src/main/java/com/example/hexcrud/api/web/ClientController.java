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
import com.example.hexcrud.domain.service.client.CreateClientService;
import com.example.hexcrud.domain.service.client.DeleteClientService;
import com.example.hexcrud.domain.service.client.FindClientByIdService;
import com.example.hexcrud.domain.service.client.ListAllClientsService;
import com.example.hexcrud.domain.service.client.UpdateClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final CreateClientService createClientService;
    private final UpdateClientService updateClientService;
    private final DeleteClientService deleteClientService;
    private final FindClientByIdService findClientByIdService;
    private final ListAllClientsService listAllClientsService;

    public ClientController(CreateClientService createClientService, UpdateClientService updateClientService,
                            DeleteClientService deleteClientService, FindClientByIdService findClientByIdService,
                            ListAllClientsService listAllClientsService) {
        this.createClientService = createClientService;
        this.updateClientService = updateClientService;
        this.deleteClientService = deleteClientService;
        this.findClientByIdService = findClientByIdService;
        this.listAllClientsService = listAllClientsService;
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequest request) {
        var input = new CreateClientService.Input(request.name(), request.email());
        var result = createClientService.execute(input);

        return switch (result) {
            case CreateClientService.Output.Created res ->
                    ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromDomain(res.client()));
            case CreateClientService.Output.EmailAlreadyExists err ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable String id, @RequestBody UpdateClientRequest request) {
        var input = new UpdateClientService.Input(id, request.name(), request.email());
        var result = updateClientService.execute(input);

        return switch (result) {
            case UpdateClientService.Output.Updated res ->
                    ResponseEntity.ok(ClientResponse.fromDomain(res.client()));
            case UpdateClientService.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
            case UpdateClientService.Output.EmailAlreadyExists err ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists", "email", err.email()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        var input = new DeleteClientService.Input(id);
        var result = deleteClientService.execute(input);

        return switch (result) {
            case DeleteClientService.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteClientService.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Client not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClient(@PathVariable String id) {
        return findClientByIdService.execute(id)
                .map(ClientResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        List<ClientResponse> clients = listAllClientsService.execute().stream()
                .map(ClientResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
}