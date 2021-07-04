package pl.edu.wat.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.services.OrdersService;
import pl.edu.wat.backend.services.ProductService;

import java.nio.file.Path;
import java.util.List;

@CrossOrigin
@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(OrdersService ordersService, ProductService productService) {
        this.productService = productService;
    }






    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") int productId){
        if(productService.deleteProduct(productId)){
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
