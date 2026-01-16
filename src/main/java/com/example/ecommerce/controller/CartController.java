package com.example.ecommerce.controller;

import com.example.ecommerce.domain.Cart;
import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    // Временно передаем userId прямо в URL, чтобы не усложнять авторизацией
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {

        Cart updatedCart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(updatedCart);
    }
    @DeleteMapping("/cleanup")
    public ResponseEntity<String> cleanupCart() {
        cartService.deleteInvalidItems();
        return ResponseEntity.ok("Битая шваль удалена успешно");
    }
    @DeleteMapping("/force-cleanup")
    public ResponseEntity<String> forceCleanup() {
        // Удаляем именно строку с ID 4 из таблицы cart_items
        cartItemRepository.deleteById(4L);
        return ResponseEntity.ok("Строка №4 удалена из корзины");
    }
}