package com.example.hexcrud.api.web.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.order.OrderStatus;

public record OrderResponse(
    String id,
    String clientId,
    List<OrderItemResponse> items,
    BigDecimal totalPrice,
    OrderStatus status,
    LocalDateTime orderDate
) {
    public static OrderResponse fromDomain(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::fromDomain)
                .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getClient().getId(), 
            itemResponses,
            order.getTotalPrice(),
            order.getStatus(),
            order.getOrderDate()
        );
    }
}