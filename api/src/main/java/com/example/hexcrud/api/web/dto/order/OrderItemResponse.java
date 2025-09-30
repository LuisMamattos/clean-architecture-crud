package com.example.hexcrud.api.web.dto.order;

import java.math.BigDecimal;

import com.example.hexcrud.domain.model.order.OrderItem;

public record OrderItemResponse(
    String productId,
    String productName,
    int quantity,
    BigDecimal priceAtTimeOfOrder,
    BigDecimal total
) {
    public static OrderItemResponse fromDomain(OrderItem item) {
        return new OrderItemResponse(
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getPriceAtTimeOfOrder(),
            item.getTotal()
        );
    }
}