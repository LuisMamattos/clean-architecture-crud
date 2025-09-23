package com.example.hexcrud.domain.service.order;
import java.util.Optional;

import com.example.hexcrud.domain.model.order.Order;
public interface FindOrderByIdService {
    Optional<Order> execute(String orderId);
}