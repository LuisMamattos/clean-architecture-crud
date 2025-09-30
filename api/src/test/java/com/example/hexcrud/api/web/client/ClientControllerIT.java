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

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Client.class);
    }

    @Test
    @DisplayName("POST /clients - Should create a client and return 201")
    void shouldCreateClient() throws Exception {
        var createRequest = new CreateClientRequest("Test Client", "test@example.com");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Client"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("POST /clients - Should return 409 when email already exists")
    void shouldReturnConflictWhenEmailExists() throws Exception {
        // Arrange: Setup direct via mongoTemplate
        mongoTemplate.save(Client.create("First Client", "duplicate@example.com"));

        var duplicateRequest = new CreateClientRequest("Second Client", "duplicate@example.com");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already in use: duplicate@example.com"));
    }
    
    @Test
    @DisplayName("GET /clients/{id} - Should return 404 for non-existent ID")
    void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/clients/{id}", "633333333333333333333333"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /clients/{id} - Should update client and return 200")
    void shouldUpdateClient() throws Exception {
        // Arrange
        Client existingClient = mongoTemplate.save(Client.create("Old Name", "old@email.com"));
        var updateRequest = new UpdateClientRequest("New Name", "new@email.com");

        mockMvc.perform(put("/clients/{id}", existingClient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@email.com"));
    }
}