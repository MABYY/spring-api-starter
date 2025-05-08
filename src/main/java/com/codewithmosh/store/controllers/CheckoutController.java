package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckoutRequestDTO;
import com.codewithmosh.store.dtos.CheckoutResponseDTO;
import com.codewithmosh.store.dtos.ErrorDTO;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;


    @PostMapping("/place")
    public CheckoutResponseDTO checkout( @Valid @RequestBody CheckoutRequestDTO request){
        return checkoutService.checkout(request);
    };

    @ExceptionHandler({CartNotFoundException.class, CartNotFoundException.class })
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }


}
