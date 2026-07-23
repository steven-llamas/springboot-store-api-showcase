package com.store_api_example.controllers;

import com.store_api_example.dtos.AddItemToCartRequest;
import com.store_api_example.dtos.AddQtyToProductRequest;
import com.store_api_example.dtos.CartDto;
import com.store_api_example.dtos.CartItemDto;
import com.store_api_example.entities.Cart;
import com.store_api_example.entities.CartItem;
import com.store_api_example.entities.Product;
import com.store_api_example.mappers.ICartMapper;
import com.store_api_example.repositories.CartRepository;
import com.store_api_example.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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
    public ResponseEntity<CartItemDto> updateCart(
            @PathVariable(name = "cartId") final UUID cartId,
            @RequestBody final AddItemToCartRequest request) {
        final var cart    = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null)
            return ResponseEntity.notFound().build();

        final var product = productRepository.findById(request.getProductId()).orElse(null);

        if (product == null)
            return ResponseEntity.badRequest().build();

        var cartItem = getCartItem(cart, product);

        if (cartItem != null) {
            final var qty = cartItem.getQuantity() + 1;
            cartItem.setQuantity(qty);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);

        final CartItemDto cartItemDto = cartMapper.toDto(cartItem);

        return  ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable final String cartId) {
        UUID idUuid;
        try {
            idUuid = UUID.fromString(cartId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        final var cart = cartRepository.getCartWithItems(idUuid).orElse(null);

        return (cart != null)
                ? ResponseEntity.ok(cartMapper.toDto(cart))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable("cartId") final UUID cartId,
            @PathVariable("productId") final Long productId,
            @Valid @RequestBody final AddQtyToProductRequest request) {

        final var cart    = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","cart not found.")
            );
        final var cartItem = getCartItem(cart, productId);

        if (cartItem == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Product not found in cart.")
            );

//        if (request.getQuantity() < 1 || request.getQuantity() > 100)
//            return ResponseEntity.badRequest().build();

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return  ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    private static CartItem getCartItem(Cart cart, Product product) {
        return (cart != null && product != null)
                ? cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null)
                : null;
    }

    private static CartItem getCartItem(Cart cart, Long productId) {
        return (cart != null && productId != null)
                ? cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null)
                : null;
    }
}
