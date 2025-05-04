package com.codewithmosh.store.dtos;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemToCartDTO {

    @NotBlank(message = "ProductId is required")
    private Long productId;

}
