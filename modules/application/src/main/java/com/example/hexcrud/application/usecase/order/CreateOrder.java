package com.example.hexcrud.application.usecase.order;
import com.example.hexcrud.domain.model.order.Order;
public interface CreateOrder {
    record Input(String clientId) {}
    Order execute(Input input);
}