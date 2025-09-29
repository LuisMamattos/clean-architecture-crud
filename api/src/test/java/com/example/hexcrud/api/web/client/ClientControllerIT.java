package com.example.hexcrud.api.web.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexcrud.api.web.dto.client.CreateClientRequest;
import com.example.hexcrud.api.web.dto.client.UpdateClientRequest;
import com.example.hexcrud.domain.model.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        var createClientRequest = new CreateClientRequest("Test Client", "test@example.com");

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
        var initialRequest = new CreateClientRequest("First Client", "duplicate@example.com");
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialRequest)))
                .andExpect(status().isCreated());

        var duplicateRequest = new CreateClientRequest("Second Client", "duplicate@example.com");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email already in use: duplicate@example.com"));
    }

    @Test
    @DisplayName("Given a non-existent ID, when finding a client, then should return status 404")
    void givenNonExistentId_whenFindingById_thenShouldReturn404() throws Exception {
        // CORREÇÃO: Usando um ID com formato válido de MongoDB ObjectId
        String nonExistentId = "633333333333333333333333";

        mockMvc.perform(get("/clients/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Client not found with ID: " + nonExistentId))
                .andExpect(jsonPath("$.path").value("/clients/" + nonExistentId));
    }

    @Test
    @DisplayName("Given a non-existent ID, when updating a client, then should return status 404")
    void givenNonExistentId_whenUpdating_thenShouldReturn404() throws Exception {
        // CORREÇÃO: Usando um ID com formato válido de MongoDB ObjectId
        String nonExistentId = "633333333333333333333333";
        var updateRequest = new UpdateClientRequest("Any Name", "any@email.com");

        mockMvc.perform(put("/clients/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                // APRIMORAMENTO: Verificando o corpo completo do erro para consistência
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Client not found with ID: " + nonExistentId))
                .andExpect(jsonPath("$.path").value("/clients/" + nonExistentId));
    }
}