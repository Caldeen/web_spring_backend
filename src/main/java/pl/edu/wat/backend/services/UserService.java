package pl.edu.wat.backend.services;

import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.entities.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    void add(User user);
    void delete(int userId);
    Set<User> getAllUsers();

    User findUserByToken(UUID token);
    User tryLogin(String username, String password);
    UserEntity findUserEntByToken(UUID token);

    UUID handleLogin(User user);

    void logout(User user);

    User findUserByUsername(String username);

    List<ProductResponse> getUsersProducts(UUID token);

    void addProduct(ProductRequest productRequest,UUID token);

    List<ProductResponse> getAllProducts(UUID token);
    List<OrderResponse> getUsersOrders(UUID token);

    void addOrder(UUID token);
}
