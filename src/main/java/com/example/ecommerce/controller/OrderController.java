package com.example.ecommerce.controller;

import com.example.ecommerce.domain.Order;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.createOrder(userId));
    }
}