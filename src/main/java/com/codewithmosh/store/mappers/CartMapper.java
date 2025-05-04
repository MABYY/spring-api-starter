package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDTO;
import com.codewithmosh.store.dtos.CartItemDTO;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping( target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDTO toDTO( Cart cart);

    @Mapping( target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDTO toCartItemDTO (CartItem cartItem);
}
