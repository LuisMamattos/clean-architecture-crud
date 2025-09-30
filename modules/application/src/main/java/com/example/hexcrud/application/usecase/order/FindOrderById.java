package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;

public interface FindOrderById {
    Order execute(String orderId);
}