package com.example.hexcrud.api.web.product;

import java.math.BigDecimal;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexcrud.api.web.dto.product.CreateProductRequest;
import com.example.hexcrud.api.web.dto.product.UpdateProductRequest;
import com.example.hexcrud.domain.model.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class ProductControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Product.class);
    }
    
    @Test
    @DisplayName("POST /products - Should create a product and return 201")
    void shouldCreateProduct() throws Exception {
        var createRequest = new CreateProductRequest("New Product", new BigDecimal("19.99"));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(19.99));
    }

    @Test
    @DisplayName("PUT /products/{id} - Should update a product and return 200")
    void shouldUpdateProduct() throws Exception {
        // Arrange
        Product existingProduct = mongoTemplate.save(Product.create("Old Name", new BigDecimal("10.00")));
        var updateRequest = new UpdateProductRequest("New Name", new BigDecimal("99.00"));

        mockMvc.perform(put("/products/{id}", existingProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProduct.getId()))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.price").value(99.00));
    }

    @Test
    @DisplayName("PUT /products/{id} - Should return 404 when product does not exist")
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        var updateRequest = new UpdateProductRequest("New Name", new BigDecimal("99.00"));

        mockMvc.perform(put("/products/{id}", "60d5ec49e9b8a22b0c14b581")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}