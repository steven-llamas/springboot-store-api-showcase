package com.store_api_example.mappers;

import com.store_api_example.dtos.CartDto;
import com.store_api_example.dtos.CartItemDto;
import com.store_api_example.dtos.CartProductDto;
import com.store_api_example.entities.Cart;
import com.store_api_example.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalCartPrice())")
    CartDto toDto(final Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(final CartItem cartItem);

    Cart    toEntity(final CartDto cartDto);
}
