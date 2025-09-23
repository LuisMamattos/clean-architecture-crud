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
import com.example.hexcrud.domain.service.order.AddItemToOrderService;
import com.example.hexcrud.domain.service.order.CancelOrderService;
import com.example.hexcrud.domain.service.order.ConfirmOrderService;
import com.example.hexcrud.domain.service.order.CreateOrderService;
import com.example.hexcrud.domain.service.order.FindOrderByIdService;
import com.example.hexcrud.domain.service.order.ListAllOrdersService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;
    private final AddItemToOrderService addItemToOrderService;
    private final FindOrderByIdService findOrderByIdService;
    private final ListAllOrdersService listAllOrdersService;
    private final ConfirmOrderService confirmOrderService;
    private final CancelOrderService cancelOrderService;

    public OrderController(CreateOrderService createOrderService, AddItemToOrderService addItemToOrderService,
                           FindOrderByIdService findOrderByIdService, ListAllOrdersService listAllOrdersService,
                           ConfirmOrderService confirmOrderService, CancelOrderService cancelOrderService) {
        this.createOrderService = createOrderService;
        this.addItemToOrderService = addItemToOrderService;
        this.findOrderByIdService = findOrderByIdService;
        this.listAllOrdersService = listAllOrdersService;
        this.confirmOrderService = confirmOrderService;
        this.cancelOrderService = cancelOrderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        var input = new CreateOrderService.Input(request.clientId());
        var order = createOrderService.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return findOrderByIdService.execute(id)
                .map(order -> ResponseEntity.ok(OrderResponse.fromDomain(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(@PathVariable String orderId, @RequestBody AddItemRequest request) {
        var input = new AddItemToOrderService.Input(orderId, request.productId(), request.quantity());
        var updatedOrder = addItemToOrderService.execute(input);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = listAllOrdersService.execute().stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String id) {
        var updatedOrder = confirmOrderService.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String id) {
        var updatedOrder = cancelOrderService.execute(id);
        return ResponseEntity.ok(OrderResponse.fromDomain(updatedOrder));
    }
}