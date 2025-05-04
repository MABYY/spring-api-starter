package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AddItemToCartDTO;
import com.codewithmosh.store.dtos.CartDTO;
import com.codewithmosh.store.dtos.CartItemDTO;
import com.codewithmosh.store.dtos.UpdateCartItemDTO;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private  ProductRepository productRepository;

    public CartDTO createCart( ) {
        var newCart = new Cart();
        cartRepository.save(newCart);
        return cartMapper.toDTO(newCart);
    };

    public CartItemDTO addItemToCart(UUID cartId, AddItemToCartDTO request) {
        var cart = cartRepository.findById(cartId).orElse(null);

        if(cart == null ){
           throw new CartNotFoundException();
        };
        var product = productRepository.findById(request.getProductId()).orElse(null);

        if( product == null){
            throw new ProductNotFoundException();
        };
//        // Check if the item is already in the cart or else add new one

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) )
                .findFirst().orElse(null);

        if(cartItem !=null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(1);

            cart.getCartItems().add(cartItem);
        };

        cartRepository.save(cart);
        return cartMapper.toCartItemDTO(cartItem);

    }

    public CartDTO getCart( UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null ){
            throw new CartNotFoundException();
        };
        return cartMapper.toDTO(cart);
    };

    public CartDTO updateCart(
            UUID cartId, Long productId, UpdateCartItemDTO request) {

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        };

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) )
                .findFirst().orElse(null);

        if( cartItem ==null ){
            throw new ProductNotFoundException();
        };

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return cartMapper.toDTO(cart);
    };

    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        };
        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) )
                .findFirst().orElse(null);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }
        cart.getCartItems().remove(cartItem);
        cartItem.setCart(null); // set orphan removal to true
        cartRepository.save(cart);
    };

    public void clearCartItems(UUID cartId ) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        };
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
