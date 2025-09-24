package com.example.hexcrud.application.usecase.order;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public class FindOrderByIdImpl implements FindOrderById {
    private final OrderRepository orderRepository;

    public FindOrderByIdImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> execute(String orderId) {
        return orderRepository.findById(orderId);
    }
}