package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.order.OrderRepository;
import com.example.hexcrud.domain.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddItemToOrderImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddItemToOrderImpl addItemToOrder;

    @Test
    @DisplayName("Given existing order and product, when adding item, then should save and return updated order")
    void given_existingOrderAndProduct_when_addingItem_then_shouldSaveAndReturnUpdatedOrder() {
        // Given (Arrange)
        var input = new AddItemToOrder.Input("order-123", "product-abc", 2);
        
        Order orderFromDb = Order.create("client-xyz");
        Product productFromDb = new Product("Test Product", 50.0);
        productFromDb.setId("product-abc");//------------------------------

        // Configuração dos Mocks
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(orderFromDb));
        when(productRepository.findById("product-abc")).thenReturn(Optional.of(productFromDb));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When (Act)
        Order updatedOrder = addItemToOrder.execute(input);

        // Then (Assert)
        assertThat(updatedOrder.getItems()).hasSize(1);
        assertThat(updatedOrder.getItems().get(0).getProductId()).isEqualTo("product-abc");
        assertThat(updatedOrder.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(updatedOrder.getTotalPrice()).isEqualByComparingTo("100.00");

        // Verificamos que o método save foi chamado exatamente uma vez.
        verify(orderRepository, times(1)).save(orderFromDb);
    }

    @Test
    @DisplayName("Given a non-existent order ID, when adding item, then should throw exception")
    void given_aNonExistentOrderId_when_addingItem_then_shouldThrowException() {
        // Given
        var input = new AddItemToOrder.Input("non-existent-order", "product-abc", 1);

        // Configuração do Mock (simulando que o pedido não foi encontrado)
        when(orderRepository.findById("non-existent-order")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> {
            addItemToOrder.execute(input);
        })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Order not found");

        // Verificamos que o repositório de produto e o método save nunca foram chamados.
        verify(productRepository, never()).findById(anyString());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Given an existing order but non-existent product ID, when adding item, then should throw exception")
    void given_existingOrderButNonExistentProduct_when_addingItem_then_shouldThrowException() {
        // Given
        var input = new AddItemToOrder.Input("order-123", "non-existent-product", 1);
        Order orderFromDb = Order.create("client-xyz");

        // Configuração dos Mocks
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(orderFromDb));
        when(productRepository.findById("non-existent-product")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> {
            addItemToOrder.execute(input);
        })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Product not found");

        // Verificamos que o método save nunca foi chamado.
        verify(orderRepository, never()).save(any(Order.class));
    }
}