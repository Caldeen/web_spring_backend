package pl.edu.wat.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.backend.dtos.OrderRequest;
import pl.edu.wat.backend.dtos.OrderResponse;
import pl.edu.wat.backend.dtos.OrderUpdateRequest;
import pl.edu.wat.backend.services.OrdersService;

import java.util.List;

@CrossOrigin
@RestController
public class OrderController {
    private OrdersService ordersService;

    public OrderController(OrdersService ordersService) {

        this.ordersService = ordersService;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(){
        List<OrderResponse> orders = ordersService.getAllOrders();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/api/orders")
    public ResponseEntity updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        ordersService.updateOrder(orderUpdateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping("/api/orders/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable int orderId){
        if(ordersService.deleteOrder(orderId)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
