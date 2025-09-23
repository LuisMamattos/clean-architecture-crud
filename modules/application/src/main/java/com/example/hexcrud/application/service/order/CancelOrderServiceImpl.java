package com.example.hexcrud.application.service.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.service.order.CancelOrderService;

public class CancelOrderServiceImpl implements CancelOrderService {
    private final OrderRepositoryPort orderRepository;

    public CancelOrderServiceImpl(OrderRepositoryPort orderRepository) {
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