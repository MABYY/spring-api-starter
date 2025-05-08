package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.ErrorDTO;
import com.codewithmosh.store.dtos.OrderDTO;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name="Orders") // Swagger
public class OrderController {

    public final OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrders(){
        return orderService.getAllOrders();
    };

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId){
        return orderService.getOrderBy(orderId);
    };

    @ExceptionHandler({OrderNotFoundException.class })
    public ResponseEntity<Void> handleErrorNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> handleAccessDenied(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO("Order not found"));
    }



}
