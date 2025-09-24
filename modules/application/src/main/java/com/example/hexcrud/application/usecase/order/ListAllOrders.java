package com.example.hexcrud.application.usecase.order;

import java.util.List;

import com.example.hexcrud.domain.model.order.Order;

public interface ListAllOrders {
    List<Order> execute();
}