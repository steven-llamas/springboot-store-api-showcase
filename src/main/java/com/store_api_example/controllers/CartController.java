package com.store_api_example.controllers;

import com.store_api_example.dtos.AddItemToCartRequest;
import com.store_api_example.dtos.CartDto;
import com.store_api_example.entities.Cart;
import com.store_api_example.entities.CartItem;
import com.store_api_example.entities.Product;
import com.store_api_example.mappers.ICartMapper;
import com.store_api_example.repositories.CartRepository;
import com.store_api_example.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartRepository    cartRepository;
    private final ICartMapper       cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(final UriComponentsBuilder uriBuilder) {
        var cart = new Cart();
        cartRepository.save(cart);
        final var cartDto = cartMapper.toDto(cart);
        final var uri = uriBuilder
                .path("/carts/{id}")
                .buildAndExpand(cartDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDto> updateCart(
            @PathVariable(name = "cartId") final UUID cartId,
            @RequestBody final AddItemToCartRequest request) {
        final var cart    = cartRepository.findById(cartId).orElse(null);

        if (cart == null)
            return ResponseEntity.notFound().build();

        final var product = productRepository.findById(request.getProductId()).orElse(null);

        if (product == null)
            return ResponseEntity.badRequest().build();

        var cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1L);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        return  ResponseEntity.ok(null);
    }

}
