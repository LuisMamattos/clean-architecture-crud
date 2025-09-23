package com.example.hexcrud.domain.service.order;
import com.example.hexcrud.domain.model.order.Order;
public interface CreateOrderService {
    record Input(String clientId) {}
    Order execute(Input input);
}