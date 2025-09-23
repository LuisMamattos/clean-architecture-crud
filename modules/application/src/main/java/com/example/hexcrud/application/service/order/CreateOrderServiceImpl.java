package com.example.hexcrud.application.service.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.service.order.CreateOrderService;

public class CreateOrderServiceImpl implements CreateOrderService {
    private final OrderRepositoryPort orderRepository;
    private final ClientRepositoryPort clientRepository;

    public CreateOrderServiceImpl(OrderRepositoryPort orderRepository, ClientRepositoryPort clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Order execute(Input input) {
        clientRepository.findById(input.clientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + input.clientId()));

        Order newOrder = Order.create(input.clientId());
        return orderRepository.save(newOrder);
    }
}