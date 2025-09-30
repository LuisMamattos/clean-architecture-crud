package com.example.hexcrud.application.usecase.order;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.order.OrderRepository;
import com.example.hexcrud.domain.repository.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
class AddItemToOrderImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private AddItemToOrderImpl addItemToOrder;

    @Test
    @DisplayName("Should add item to order successfully")
    void shouldAddItemToOrderSuccessfully() {
        // Arrange
        var input = new AddItemToOrder.Input("order-123", "product-abc", 2);
        
        Client client = Client.create("client-xyz", "client@test.com");
        Order orderFromDb = Order.create(client);
        Product productFromDb = Product.create("Test Product", new BigDecimal("50.00"));
        
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(orderFromDb));
        when(productRepository.findById("product-abc")).thenReturn(Optional.of(productFromDb));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = addItemToOrder.execute(input);

        // Assert
        assertThat(updatedOrder.getItems()).hasSize(1);
        assertThat(updatedOrder.getItems().get(0).getProduct()).isEqualTo(productFromDb);
        assertThat(updatedOrder.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(updatedOrder.getTotalPrice()).isEqualByComparingTo("100.00");
        verify(orderRepository, times(1)).save(orderFromDb);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for non-existent order")
    void shouldThrowNotFoundForNonExistentOrder() {
        // Arrange
        var input = new AddItemToOrder.Input("non-existent-order", "product-abc", 1);
        when(orderRepository.findById("non-existent-order")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> addItemToOrder.execute(input))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Order not found");

        verify(productRepository, never()).findById(anyString());
        verify(orderRepository, never()).save(any(Order.class));
    }
}