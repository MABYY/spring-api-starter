package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutRequestDTO;
import com.codewithmosh.store.dtos.CheckoutResponseDTO;
import com.codewithmosh.store.dtos.ErrorDTO;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {

    private  OrderRepository orderRepository;
    private  CartRepository cartRepository;

    private  AuthService authService;
    private  CartService cartService;


    public CheckoutResponseDTO checkout(CheckoutRequestDTO request){

        var cart = cartRepository.findById(request.getCartId()).orElse(null); // check
        if(cart == null){
            throw new CartNotFoundException();
        };
        if (cart.isEmptyCart()){
            throw new CartEmptyException();
//            return ResponseEntity.badRequest().body(
//                    new ErrorDTO("Cart is empty")
//            );
        }
        var order = Order.fromCart(cart,authService.getCurrentUser() );
        // Save order
        orderRepository.save(order);

        // Clear the cart
        cartService.clearCartItems(cart.getId());

        return new CheckoutResponseDTO(order.getId());
    }
}
