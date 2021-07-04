package pl.edu.wat.backend.services;

import pl.edu.wat.backend.dtos.OrderRequest;
import pl.edu.wat.backend.dtos.OrderResponse;
import pl.edu.wat.backend.dtos.OrderUpdateRequest;
import pl.edu.wat.backend.entities.OrderEntity;

import java.util.List;


public interface OrdersService {
    List<OrderResponse> getAllOrders();

    void addOrder(OrderRequest orderRequest);

    boolean deleteOrder(int orderRequest);

    void updateOrder(OrderUpdateRequest orderUpdateRequest);
}
