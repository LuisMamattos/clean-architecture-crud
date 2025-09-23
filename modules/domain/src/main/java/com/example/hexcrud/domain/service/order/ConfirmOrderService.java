package com.example.hexcrud.domain.service.order;

import com.example.hexcrud.domain.model.order.Order;

public interface ConfirmOrderService {
    /**
     * Confirma um pedido, mudando seu estado de PENDING para CONFIRMED.
     * @param orderId O ID do pedido a ser confirmado.
     * @return O pedido atualizado.
     * @throws IllegalStateException se o pedido não estiver no estado PENDING.
     */
    Order execute(String orderId);
}