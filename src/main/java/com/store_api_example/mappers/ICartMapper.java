package com.store_api_example.mappers;

import com.store_api_example.dtos.CartDto;
import com.store_api_example.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartMapper {
    CartDto toDto(final Cart cart);
    Cart    toEntity(final CartDto cartDto);
}
