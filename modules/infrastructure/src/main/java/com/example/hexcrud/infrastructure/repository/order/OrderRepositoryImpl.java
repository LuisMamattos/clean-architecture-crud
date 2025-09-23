package com.example.hexcrud.infrastructure.repository.order;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;


public class OrderRepositoryImpl implements OrderRepositoryPort {

    private final OrderMongoRepository mongoRepository;

    public OrderRepositoryImpl(OrderMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Order save(Order order) {
        return mongoRepository.save(order);
    }
    @Override
    public Optional<Order> findById(String id) {
        return mongoRepository.findById(id);
    }
    @Override
    public List<Order> findAll() {
        return mongoRepository.findAll();
    }
}