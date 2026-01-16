package com.example.ecommerce.service;

import com.example.ecommerce.domain.*;
import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final EntityManager entityManager;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    // Добавь это в начало класса CartService к остальным приватным полям
    private final CartItemRepository cartItemRepository;
    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public void deleteInvalidItems() {
        // Этот запрос удалит вообще всё, где product_id = null или где товар с id=4 (битый)
        // Мы используем нативный SQL, чтобы обойти кэш Hibernate
        entityManager.createNativeQuery("DELETE FROM cart_items WHERE product_id IS NULL OR id = 4").executeUpdate();
    }
    @Transactional
    public Cart addToCart(Long userId, AddToCartRequest request) {
        Cart cart = getCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder().cart(cart).product(product).quantity(0).build();
                    cart.getItems().add(newItem);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.getQuantity());
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}