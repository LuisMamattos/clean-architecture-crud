package com.example.hexcrud.domain.model.order;

import java.math.BigDecimal;

import org.springframework.data.annotation.PersistenceCreator;

import com.example.hexcrud.domain.exception.DomainValidationException; // Import necessário
import com.example.hexcrud.domain.model.product.Product;
import org.springframework.data.mongodb.core.mapping.DBRef; 

public class OrderItem {
    
    @DBRef
    private Product product;
    private int quantity;
    private BigDecimal priceAtTimeOfOrder; // "Congela" o preço do produto no momento da compra

    // --- CONSTRUTORES ---

    // Construtor para a lógica de negócio (usado pelo factory)
    private OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtTimeOfOrder = product.getPrice(); // Captura o preço atual do produto
    }

    // Construtor PARA A PERSISTÊNCIA. O Spring Data usará este.
    // Ele recebe todos os campos que são salvos no banco de dados.
    @PersistenceCreator
    private OrderItem(Product product, int quantity, BigDecimal priceAtTimeOfOrder) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtTimeOfOrder = priceAtTimeOfOrder;
    }

    // Construtor sem argumentos para o framework
    private OrderItem() {}

    // --- FACTORY METHOD ---

    public static OrderItem create(Product product, int quantity) {
        if (product == null) {
            throw new DomainValidationException("Product cannot be null for an OrderItem.");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity must be positive.");
        }
        return new OrderItem(product, quantity);
    }

    // --- MÉTODOS DE COMPORTAMENTO ---

    public BigDecimal getTotal() {
        return priceAtTimeOfOrder.multiply(BigDecimal.valueOf(quantity));
    }
    
    // --- GETTERS ---

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceAtTimeOfOrder() { return priceAtTimeOfOrder; }
}