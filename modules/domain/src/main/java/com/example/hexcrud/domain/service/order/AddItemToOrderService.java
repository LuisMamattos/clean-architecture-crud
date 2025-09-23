package com.example.hexcrud.domain.service.order;
import com.example.hexcrud.domain.model.order.Order;
public interface AddItemToOrderService {
    record Input(String orderId, String productId, int quantity) {}
    Order execute(Input input);
}