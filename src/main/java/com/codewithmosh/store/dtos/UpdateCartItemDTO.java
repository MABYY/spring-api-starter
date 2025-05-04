package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCartItemDTO {
    @Min(value=1, message = "Quantity greater than zero")
    @Max(value=100, message = "Quantity  smaller than 100")
    public Integer quantity;
}
