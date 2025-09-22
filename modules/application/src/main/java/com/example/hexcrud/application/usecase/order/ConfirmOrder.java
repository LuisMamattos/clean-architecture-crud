package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.port.in.order.ConfirmOrderUseCase;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;

public class ConfirmOrder implements ConfirmOrderUseCase {

    private final OrderRepositoryPort orderRepository;

    public ConfirmOrder(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order execute(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.confirm();
        
        return orderRepository.save(order);
    }
}