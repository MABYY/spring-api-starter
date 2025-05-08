package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.OrderDTO;
import com.codewithmosh.store.entities.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toOrderDTO( Order order);
}
