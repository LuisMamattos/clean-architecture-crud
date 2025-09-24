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

import com.example.hexcrud.api.web.dto.order.AddItemRequest;
import com.example.hexcrud.api.web.dto.order.CreateOrderRequest;
import com.example.hexcrud.api.web.dto.order.OrderResponse;
import com.example.hexcrud.application.usecase.order.AddItemToOrder;
import com.example.hexcrud.application.usecase.order.CancelOrder;
import com.example.hexcrud.application.usecase.order.ConfirmOrder;
import com.example.hexcrud.application.usecase.order.CreateOrder;
import com.example.hexcrud.application.usecase.order.FindOrderById;
import com.example.hexcrud.application.usecase.order.ListAllOrders;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrder createOrder;
    private final AddItemToOrder addItemToOrder;
    private final FindOrderById findOrderById;
    private final ListAllOrders listAllOrders;
    private final ConfirmOrder confirmOrder;
    private final CancelOrder cancelOrder;

    public OrderController(CreateOrder createOrder, AddItemToOrder addItemToOrder,
                           FindOrderById findOrderById, ListAllOrders listAllOrders,
                           ConfirmOrder confirmOrder, CancelOrder cancelOrder) {
        this.createOrder = createOrder;
        this.addItemToOrder = addItemToOrder;
        this.findOrderById = findOrderById;
        this.listAllOrders = listAllOrders;
        this.confirmOrder = confirmOrder;
        this.cancelOrder = cancelOrder;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        var input = new CreateOrder.Input(request.clientId());
        var order = createOrder.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return findOrderById.execute(id)
                .map(order -> ResponseEntity.ok(OrderResponse.fromDomain(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(@PathVariable String orderId, @RequestBody AddItemRequest request) {
        var input = new AddItemToOrder.Input(orderId, request.productId(), request.quantity());
        var updatedOrder = addItemToOrder.execute(input);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = listAllOrders.execute().stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String id) {
        var updatedOrder = confirmOrder.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String id) {
        var updatedOrder = cancelOrder.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }
}