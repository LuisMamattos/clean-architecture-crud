package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.port.in.order.CancelOrderUseCase;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;

public class CancelOrder implements CancelOrderUseCase {
    private final OrderRepositoryPort orderRepository;

    public CancelOrder(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order execute(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.cancel();

        return orderRepository.save(order);
    }
}