package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;

public interface CancelOrder {
    /**
     * Cancela um pedido, mudando seu estado para CANCELLED.
     * @param orderId O ID do pedido a ser cancelado.
     * @return O pedido atualizado.
     * @throws IllegalStateException se o pedido já estiver cancelado.
     */
    Order execute(String orderId);
}