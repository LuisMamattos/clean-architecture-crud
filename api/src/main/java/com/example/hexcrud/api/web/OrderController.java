package com.example.hexcrud.api.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexcrud.api.web.dto.order.AddItemToOrderRequest;
import com.example.hexcrud.api.web.dto.order.CreateOrderRequest;
import com.example.hexcrud.api.web.dto.order.OrderResponse;
import com.example.hexcrud.application.usecase.order.AddItemToOrder;
import com.example.hexcrud.application.usecase.order.CancelOrder;
import com.example.hexcrud.application.usecase.order.ConfirmOrder;
import com.example.hexcrud.application.usecase.order.CreateOrder;
import com.example.hexcrud.application.usecase.order.FindOrderById;
import com.example.hexcrud.application.usecase.order.ListAllOrders;
import com.example.hexcrud.domain.model.order.Order;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrder createOrder;
    private final AddItemToOrder addItemToOrder;
    private final ConfirmOrder confirmOrder;
    private final CancelOrder cancelOrder;
    private final FindOrderById findOrderById;
    private final ListAllOrders listAllOrders;

    public OrderController(CreateOrder createOrder, AddItemToOrder addItemToOrder, ConfirmOrder confirmOrder, CancelOrder cancelOrder, FindOrderById findOrderById, ListAllOrders listAllOrders) {
        this.createOrder = createOrder;
        this.addItemToOrder = addItemToOrder;
        this.confirmOrder = confirmOrder;
        this.cancelOrder = cancelOrder;
        this.findOrderById = findOrderById;
        this.listAllOrders = listAllOrders;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        var input = new CreateOrder.Input(request.clientId());
        Order order = createOrder.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable String id) {
        Order order = findOrderById.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponse>> listAll() {
        List<OrderResponse> orders = listAllOrders.execute().stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItem(@PathVariable String orderId, @RequestBody @Valid AddItemToOrderRequest request) {
        var input = new AddItemToOrder.Input(orderId, request.productId(), request.quantity());
        Order order = addItemToOrder.execute(input);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable String orderId) {
        Order order = confirmOrder.execute(orderId);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable String orderId) {
        Order order = cancelOrder.execute(orderId);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }
}