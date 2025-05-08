package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
  private OrderProductDTO product;
//   private Long id;
//    private String name;
//    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;

}
