package pl.edu.wat.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.services.OrdersService;
import pl.edu.wat.backend.services.ProductService;

import java.util.List;

@RestController
public class DemoController {

    private OrdersService ordersService;
    private ProductService productService;

    @Autowired
    public DemoController( OrdersService ordersService, ProductService productService) {
        this.ordersService = ordersService;
        this.productService = productService;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(){
        List<OrderResponse> orders = ordersService.getAllOrders();

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @PostMapping("/api/orders")
    public ResponseEntity addOrder(@RequestBody OrderRequest orderRequest) {
        ordersService.addOrder(orderRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> demos = productService.getAllProducts();

        return new ResponseEntity<>(demos, HttpStatus.OK);
    }

    @PostMapping("/api/products")
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    @DeleteMapping("/api/orders")
    public ResponseEntity deleteOrder(@RequestBody OrderRequest orderRequest){
        if(ordersService.deleteOrder(orderRequest)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/api/orders")
    public ResponseEntity updateOrder(@RequestBody OrderRequest orderRequest) {
        ordersService.updateOrder(orderRequest);

        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping("/api/products")
    public ResponseEntity deleteProduct(@RequestBody ProductRequest productRequest){
        if(productService.deleteProduct(productRequest)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/api/products")
    public ResponseEntity updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest) {
        productService.updateProduct(productUpdateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }
}
