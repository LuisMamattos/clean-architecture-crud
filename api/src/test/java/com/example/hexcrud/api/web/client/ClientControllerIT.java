package com.example.hexcrud.api.web.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexcrud.api.web.dto.client.CreateClientRequest;
import com.example.hexcrud.domain.model.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.mongodb.core.MongoTemplate; 

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Client.class);
    }

    @Test
    @DisplayName("Given a valid new client, when creating, then should return status 201 and the created client")
    void given_aValidNewClient_when_creating_then_shouldReturnStatus201AndTheCreatedClient() throws Exception {
        // Dado
        var createClientRequest = new CreateClientRequest("Test Client", "test@example.com");

        // Quando & Então
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createClientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Client"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Given an existing email, when creating a client with the same email, then should return status 409")
    void given_anExistingEmail_when_creatingClientWithSameEmail_then_shouldReturnStatus409() throws Exception {
        // Dado: Um cliente já existente no banco de dados.
        var initialRequest = new CreateClientRequest("First Client", "duplicate@example.com");
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialRequest)))
                .andExpect(status().isCreated());

        // Quando: Tentamos criar um segundo cliente com o mesmo e-mail.
        var duplicateRequest = new CreateClientRequest("Second Client", "duplicate@example.com");

        // Então: A API deve retornar um erro de conflito.
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }
}