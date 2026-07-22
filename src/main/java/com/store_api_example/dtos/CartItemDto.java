package com.store_api_example.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private CartProductDto  product;
    private Long            quantity;
    private BigDecimal      totalPrice;
}
