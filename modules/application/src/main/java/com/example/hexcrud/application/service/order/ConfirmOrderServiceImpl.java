package com.example.hexcrud.application.service.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.service.order.ConfirmOrderService;

public class ConfirmOrderServiceImpl implements ConfirmOrderService {

    private final OrderRepositoryPort orderRepository;

    public ConfirmOrderServiceImpl(OrderRepositoryPort orderRepository) {
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