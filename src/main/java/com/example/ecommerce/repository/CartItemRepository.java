package com.example.ecommerce.repository;

import com.example.ecommerce.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE product_id IS NULL", nativeQuery = true)
    void deleteItemsWithNullProduct();
}