package com.example.hexcrud.application.usecase.order;
import com.example.hexcrud.domain.model.order.Order;
public interface AddItemToOrder {
    record Input(String orderId, String productId, int quantity) {}
    Order execute(Input input);
}