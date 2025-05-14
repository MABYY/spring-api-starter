package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.OrderDTO;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrdersMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    public final AuthService authService;
    public final OrderRepository orderRepository;
    private final OrdersMapper ordersMapper;

    public List<OrderDTO> getAllOrders(){
        var user = authService.getCurrentUser();
        // N+1 problem. Lazy loading by default because working with collections
        var orders = orderRepository.getAllByCustomer(user);
        // In order to map each order, Hibernate fetches each order in the database
        // Solve the eager problem using a new request in the order repository
        return orders.stream().map(ordersMapper::toOrderDTO).toList();
    }

    public OrderDTO getOrderBy(Long id){
        var order = orderRepository.getOrderWithItems(id)
                .orElseThrow(OrderNotFoundException::new);

        System.out.println(order);

        var user = authService.getCurrentUser();
        if(!order.isPlacedByUser(user)){
            throw new AccessDeniedException("Access to order denied.");
        };
        return ordersMapper.toOrderDTO(order);
    };
}
