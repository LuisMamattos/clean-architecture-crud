package com.example.hexcrud.api.web.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexcrud.api.web.dto.product.UpdateProductRequest;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.infrastructure.repository.ProductRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Should update a product successfully and return status 200")
    void shouldUpdateProductSuccessfully() throws Exception {
        // Arrange: Crie um produto usando a nova dependência
        Product existingProduct = productRepository.save(new Product("Old Name", 10.0));
        String productId = existingProduct.getId();

        var updateRequest = new UpdateProductRequest("New Name", 99.99);

        // Act & Assert
        mockMvc.perform(
                put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    @DisplayName("Should return status 404 when trying to update a non-existent product")
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        // Arrange
        String nonExistentId = "60d5ec49e9b8a22b0c14b581"; // Um ID MongoDB válido, mas inexistente
        var updateRequest = new UpdateProductRequest("New Name", 99.99);

        // Act & Assert
        mockMvc.perform(put("/products/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found"));
    }
}