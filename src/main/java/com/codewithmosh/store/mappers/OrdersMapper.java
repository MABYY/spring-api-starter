package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.OrderDTO;
import com.codewithmosh.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrderDTO toOrderDTO(Order order);
}
