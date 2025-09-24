package com.example.hexcrud.domain.repository.order;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
}