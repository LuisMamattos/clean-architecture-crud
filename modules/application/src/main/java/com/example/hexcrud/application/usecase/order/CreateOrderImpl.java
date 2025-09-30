package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.client.Client; // Importamos Client
import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.client.ClientRepository;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public class CreateOrderImpl implements CreateOrder {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public CreateOrderImpl(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Order execute(Input input) {
        Client client = clientRepository.findById(input.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create order: Client not found with id: " + input.clientId()));

        Order newOrder = Order.create(client);

        return orderRepository.save(newOrder);
    }
}