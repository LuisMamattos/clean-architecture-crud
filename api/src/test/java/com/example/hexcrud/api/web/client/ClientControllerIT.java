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
import com.example.hexcrud.infrastructure.repository.client.ClientMongoRepository;
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
    private ClientMongoRepository clientMongoRepository;

    @BeforeEach
    void setUp() {
        // Garante que a coleção de clientes esteja vazia antes de cada teste.
        clientMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a client successfully and return status 201")
    void shouldCreateClientSuccessfully() throws Exception {
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
    @DisplayName("Should fail to create a client with a duplicate email and return status 409")
    void shouldFailToCreateClientWithDuplicateEmail() throws Exception {
        // Arrange: Create an initial client to occupy the email address.
        var initialRequest = new CreateClientRequest("First Client", "duplicate@example.com");
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialRequest)))
                .andExpect(status().isCreated());

        // Act & Assert: Attempt to create a second client with the same email.
        var duplicateRequest = new CreateClientRequest("Second Client", "duplicate@example.com");
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }
}