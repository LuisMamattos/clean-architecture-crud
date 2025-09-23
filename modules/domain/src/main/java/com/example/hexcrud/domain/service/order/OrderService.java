package com.example.hexcrud.domain.service.order;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;

public interface OrderService {   
    Order createOrder(Order order);
    Optional<Order> findOrderById(String id);
    List<Order> findAllOrders();
}