package com.example.hexcrud.application.usecase.order;
import java.util.List;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public class ListAllOrdersImpl implements ListAllOrders {

    private final OrderRepository orderRepository;

    public ListAllOrdersImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute() {
        return orderRepository.findAll();
    }
}