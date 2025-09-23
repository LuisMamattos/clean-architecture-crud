package com.example.hexcrud.application.service.order;

import java.util.List;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.service.order.ListAllOrdersService;

public class ListAllOrdersServiceImpl implements ListAllOrdersService {

    private final OrderRepositoryPort orderRepository;

    public ListAllOrdersServiceImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute() {
        return orderRepository.findAll();
    }
}