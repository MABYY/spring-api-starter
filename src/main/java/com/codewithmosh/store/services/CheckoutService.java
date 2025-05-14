package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutRequestDTO;
import com.codewithmosh.store.dtos.CheckoutResponseDTO;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final AuthService authService;
    private final CartService cartService;
    private StripePaymentGateway stripePaymentGateway;


    @Transactional
    public CheckoutResponseDTO checkout(CheckoutRequestDTO request)  {

        var cart = cartRepository.findById(request.getCartId()).orElse(null); // check
        if(cart == null){
            throw new CartNotFoundException();
        };
        if (cart.isEmptyCart()){
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart,authService.getCurrentUser() );
        // Save order
        orderRepository.save(order);

        try{

            var session = stripePaymentGateway.createCheckoutSession(order);

            // Clear the cart
            cartService.clearCartItems(cart.getId());

            return new CheckoutResponseDTO(order.getId(), session.getCheckoutUrl());
        }
        catch (PaymentException ex) {
            orderRepository.delete(order); // order is Transient here, not attached to a persistent context here
                                            // Include @Transactional  to make the persistent context open during the entire method
            throw ex;
        }

    };



}
