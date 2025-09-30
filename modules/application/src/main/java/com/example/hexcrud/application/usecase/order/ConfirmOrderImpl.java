package com.example.hexcrud.application.usecase.order;
import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public class ConfirmOrderImpl implements ConfirmOrder {

    private final OrderRepository orderRepository;

    public ConfirmOrderImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order execute(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        order.confirm();
        
        return orderRepository.save(order);
    }
}