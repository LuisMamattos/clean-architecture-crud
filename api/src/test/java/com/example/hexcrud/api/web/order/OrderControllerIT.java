package com.example.hexcrud.api.web.order;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexcrud.api.web.dto.order.AddItemToOrderRequest;
import com.example.hexcrud.api.web.dto.order.CreateOrderRequest;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class OrderControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MongoTemplate mongoTemplate;

    private Client testClient;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Order.class);
        mongoTemplate.dropCollection(Product.class);
        mongoTemplate.dropCollection(Client.class);

        testClient = mongoTemplate.save(Client.create("Test Client", "client@test.com"));
        testProduct = mongoTemplate.save(Product.create("Test Product", new BigDecimal("12.50")));
    }

    @Test
    @DisplayName("POST /orders - Should create a new order and return 201")
    void shouldCreateNewOrder() throws Exception {
        var createRequest = new CreateOrderRequest(testClient.getId());

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.clientId").value(testClient.getId()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(0));
    }

    @Test
    @DisplayName("POST /orders/{id}/items - Should add an item to an order and return 200")
    void shouldAddItemToOrder() throws Exception {
        Order testOrder = mongoTemplate.save(Order.create(testClient));
        var addItemRequest = new AddItemToOrderRequest(testProduct.getId(), 2);

        mockMvc.perform(post("/orders/{orderId}/items", testOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addItemRequest)))
                .andExpect(status().isOk())
                // LINHA CORRIGIDA
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].productId").value(testProduct.getId()))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(25.00));
    }

    @Test
    @DisplayName("POST /orders/{id}/confirm - Should confirm a pending order and return 200")
    void shouldConfirmOrder() throws Exception {
        Order testOrder = Order.create(testClient);
        testOrder.addItem(testProduct, 1);
        mongoTemplate.save(testOrder);

        mockMvc.perform(post("/orders/{orderId}/confirm", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("POST /orders/{id}/confirm - Should return 422 when confirming an empty order")
    void shouldNotConfirmEmptyOrder() throws Exception {
        Order emptyOrder = mongoTemplate.save(Order.create(testClient));

        mockMvc.perform(post("/orders/{orderId}/confirm", emptyOrder.getId()))
                .andExpect(status().isUnprocessableEntity());
    }
}