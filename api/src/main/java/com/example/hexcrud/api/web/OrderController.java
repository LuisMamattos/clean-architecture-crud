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
import com.example.hexcrud.domain.port.in.order.AddItemToOrderUseCase;
import com.example.hexcrud.domain.port.in.order.CancelOrderUseCase;
import com.example.hexcrud.domain.port.in.order.ConfirmOrderUseCase;
import com.example.hexcrud.domain.port.in.order.CreateOrderUseCase;
import com.example.hexcrud.domain.port.in.order.FindOrderByIdUseCase;
import com.example.hexcrud.domain.port.in.order.ListAllOrdersUseCase;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final FindOrderByIdUseCase findOrderByIdUseCase;
    private final ListAllOrdersUseCase listAllOrdersUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;   

     public OrderController(CreateOrderUseCase createOrderUseCase, AddItemToOrderUseCase addItemToOrderUseCase,
                           FindOrderByIdUseCase findOrderByIdUseCase, ListAllOrdersUseCase listAllOrdersUseCase,
                           ConfirmOrderUseCase confirmOrderUseCase, CancelOrderUseCase cancelOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.findOrderByIdUseCase = findOrderByIdUseCase;
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase; 
        this.cancelOrderUseCase = cancelOrderUseCase;   
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        var input = new CreateOrderUseCase.Input(request.clientId());
        var order = createOrderUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return findOrderByIdUseCase.execute(id)
                .map(order -> ResponseEntity.ok(OrderResponse.fromDomain(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(@PathVariable String orderId, @RequestBody AddItemRequest request) {
        var input = new AddItemToOrderUseCase.Input(orderId, request.productId(), request.quantity());
        var updatedOrder = addItemToOrderUseCase.execute(input);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = listAllOrdersUseCase.execute().stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String id) {
        var updatedOrder = confirmOrderUseCase.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String id) {
        var updatedOrder = cancelOrderUseCase.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }
}