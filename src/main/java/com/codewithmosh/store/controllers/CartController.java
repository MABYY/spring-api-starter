package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartDTO;
import com.codewithmosh.store.dtos.CartDTO;
import com.codewithmosh.store.dtos.CartItemDTO;
import com.codewithmosh.store.dtos.UpdateCartItemDTO;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name="Carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/new")
    public ResponseEntity<CartDTO> createCart (
            UriComponentsBuilder uriBuilder
    ){
        var cartDTO = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDTO);
    }

    @PostMapping("/items/{cartId}")
    public ResponseEntity<CartItemDTO> addItemToCart (
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartDTO request
    ){
        var cartItemDTO = cartService.addItemToCart(cartId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);
    };

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart( @PathVariable UUID cartId) {
        var cartDTO = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable UUID cartId, @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemDTO request
            ) {
        var cartDTO = cartService.updateCart(cartId,productId,request);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();

    };

    @DeleteMapping("/items/{cartId}")
    public ResponseEntity<?> clearCartItems( @PathVariable UUID cartId ) {
        cartService.clearCartItems(cartId);
        return ResponseEntity.noContent().build();
    };

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error","Cart not found"));
    };

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error","Product not found in the cart"));
    };


}
