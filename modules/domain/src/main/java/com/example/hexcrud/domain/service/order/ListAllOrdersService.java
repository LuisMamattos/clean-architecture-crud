package com.example.hexcrud.domain.service.order;

import java.util.List;

import com.example.hexcrud.domain.model.order.Order;

public interface ListAllOrdersService {
    List<Order> execute();
}