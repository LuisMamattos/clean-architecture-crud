package com.example.hexcrud.application.usecase.order;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
public interface FindOrderById {
    Optional<Order> execute(String orderId);
}