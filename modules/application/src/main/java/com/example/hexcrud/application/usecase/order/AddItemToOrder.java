package com.example.hexcrud.application.usecase.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.port.in.order.AddItemToOrderUseCase;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;

public class AddItemToOrder implements AddItemToOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final ProductRepositoryPort productRepository;

    public AddItemToOrder(OrderRepositoryPort orderRepository, ProductRepositoryPort productRepository) {
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