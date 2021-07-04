package pl.edu.wat.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestHeader("SESSION") UUID token) {
        List<ProductResponse> demos = userService.getAllProducts(token);

        return new ResponseEntity<>(demos, HttpStatus.OK);
    }
    @PostMapping("/api/products")
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest,
                                     @RequestHeader("SESSION") UUID token) {
        userService.addProduct(productRequest,token);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("/api/logout")
    public ResponseEntity<String> logout(@RequestHeader("SESSION") UUID token) {
        User user = userService.findUserByToken(token);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            userService.logout(user);
            return new ResponseEntity<String>("logged out", HttpStatus.OK);
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<RegisterRequest> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.add(User.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .build());
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest);
    }
    @GetMapping("api/userProducts")
    public ResponseEntity<List<ProductResponse >> getUsersProducts(@RequestHeader("SESSION") UUID token){
        List<ProductResponse> products = userService.getUsersProducts(token);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping("api/userOrders")
    public ResponseEntity<List<OrderResponse >> getUsersOrders(@RequestHeader("SESSION") UUID token){
        List<OrderResponse> orders = userService.getUsersOrders(token);
        return new ResponseEntity<>(orders  ,HttpStatus.OK);
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginRequest> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.tryLogin(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginRequest("none", "none"));
        } else {
            UUID token = userService.handleLogin(user);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("SESSION", token.toString());
            httpHeaders.setAccessControlExposeHeaders(Arrays.asList("session","connection"));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders)
                    .body(new LoginRequest(loginRequest.getUsername(),
                            loginRequest.getPassword())
                    );
        }
    }
    @PostMapping("/api/orders")
    public ResponseEntity addOrder(@RequestHeader("SESSION") UUID token) {
        userService.addOrder(token);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
