package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalPrice;
}
