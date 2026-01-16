package com.example.ecommerce.service;

import com.example.ecommerce.domain.*;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(Long userId) {
        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        Order order = Order.builder()
                .user(cart.getUser())
                .status("PENDING")
                .build();

        // 1. Рассчитываем общую сумму заказа
        java.math.BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new java.math.BigDecimal(item.getQuantity())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        order.setTotalPrice(total); // Устанавливаем итоговую цену!

        // 2. Создаем элементы заказа (как и было)
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Недостаточно товара: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();
        }).collect(java.util.stream.Collectors.toList());

        order.setItems(orderItems);

        // Теперь save(order) сработает, так как totalPrice больше не null
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        return savedOrder;
    }

}