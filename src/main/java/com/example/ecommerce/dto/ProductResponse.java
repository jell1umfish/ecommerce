package com.example.ecommerce.dto;

import com.example.ecommerce.domain.Product.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductStatus status;
}