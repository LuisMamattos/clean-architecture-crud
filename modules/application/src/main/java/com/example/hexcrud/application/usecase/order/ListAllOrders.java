package com.example.hexcrud.application.usecase.order;

import java.util.List;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.port.in.order.ListAllOrdersUseCase;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;

public class ListAllOrders implements ListAllOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    public ListAllOrders(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute() {
        return orderRepository.findAll();
    }
}