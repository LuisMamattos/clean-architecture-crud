package com.example.hexcrud.application.service.order;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.order.AddItemToOrderService;

public class AddItemToOrderServiceImpl implements AddItemToOrderService {

    private final OrderRepositoryPort orderRepository;
    private final ProductRepositoryPort productRepository;

    public AddItemToOrderServiceImpl(OrderRepositoryPort orderRepository, ProductRepositoryPort productRepository) {
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