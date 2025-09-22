package com.example.hexcrud.application.usecase.order;

import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.port.in.order.FindOrderByIdUseCase;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;

public class FindOrderById implements FindOrderByIdUseCase {
    private final OrderRepositoryPort orderRepository;

    public FindOrderById(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> execute(String orderId) {
        return orderRepository.findById(orderId);
    }
}