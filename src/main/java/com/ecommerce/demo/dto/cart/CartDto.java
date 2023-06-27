package com.ecommerce.demo.dto.cart;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    private List<CartItemDto> cartItems;
    private double totalCost;

}
