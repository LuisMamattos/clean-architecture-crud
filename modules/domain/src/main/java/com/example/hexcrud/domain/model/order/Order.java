package com.example.hexcrud.domain.model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.hexcrud.domain.exception.DomainValidationException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.model.product.Product;

@Document(collection = "order")
public class Order {

    private String id;
    @DBRef
    private Client client;
    // O campo 'items' não pode ser final para que o construtor de persistência funcione perfeitamente.
    // A proteção é garantida pelo acesso private e pelo getter que retorna uma lista não modificável.
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime orderDate;

    // --- CONSTRUTORES ---

    // Construtor para a lógica de negócio (usado pelo factory method)
    private Order(Client client) {
        this.client = client;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        calculateTotal();
    }

    // Construtor para a PERSISTÊNCIA. O Spring Data usará este.
    @PersistenceCreator
    private Order(String id, Client client, List<OrderItem> items, BigDecimal totalPrice, OrderStatus status, LocalDateTime orderDate) {
        this.id = id;
        this.client = client;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Construtor sem argumentos para o framework
    private Order() {}

    // --- FACTORY METHOD ---

    public static Order create(Client client) {
        if (client == null) {
            throw new DomainValidationException("Client cannot be null.");
        }
        return new Order(client);
    }

    // --- MÉTODOS DE COMPORTAMENTO ---

    public void addItem(Product product, int quantity) {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainValidationException("Cannot add items to an order that is not PENDING.");
        }
        
        this.items.stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst()
            .ifPresent(item -> {
                throw new DomainValidationException("Product already in order. Use update quantity instead.");
            });

        this.items.add(OrderItem.create(product, quantity));
        calculateTotal();
    }
    
    public void removeItem(String productId) {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainValidationException("Cannot remove items from an order that is not PENDING.");
        }
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        calculateTotal();
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainValidationException("Only PENDING orders can be confirmed.");
        }
        if (this.items.isEmpty()) {
            throw new DomainValidationException("Cannot confirm an empty order.");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.CANCELLED) {
            throw new DomainValidationException("Shipped or already cancelled orders cannot be cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    private void calculateTotal() {
        this.totalPrice = items.stream()
                               .map(OrderItem::getTotal)
                               .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // --- GETTERS ---

    public String getId() { return id; }
    public Client getClient() { return client; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
}