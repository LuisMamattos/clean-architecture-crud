package com.example.hexcrud.application.service.order;

import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.service.order.FindOrderByIdService;

public class FindOrderByIdServiceImpl implements FindOrderByIdService {
    private final OrderRepositoryPort orderRepository;

    public FindOrderByIdServiceImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> execute(String orderId) {
        return orderRepository.findById(orderId);
    }
}