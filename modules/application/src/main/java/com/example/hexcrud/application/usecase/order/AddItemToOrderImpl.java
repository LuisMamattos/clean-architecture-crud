package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.order.OrderRepository;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class AddItemToOrderImpl implements AddItemToOrder {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public AddItemToOrderImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order execute(Input input) {
        Order order = orderRepository.findById(input.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + input.orderId()));

        Product product = productRepository.findById(input.productId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + input.productId()));

        order.addItem(product, input.quantity());

        return orderRepository.save(order);
    }
}