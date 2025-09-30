package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public class FindOrderByIdImpl implements FindOrderById {
    private final OrderRepository orderRepository;

    public FindOrderByIdImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order execute(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
}