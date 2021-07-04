package pl.edu.wat.backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.entities.OrderEntity;
import pl.edu.wat.backend.entities.ProductEntity;
import pl.edu.wat.backend.entities.UserEntity;
import pl.edu.wat.backend.repositories.UserRepository;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public void add(User user) {
        if (findUserByUsername(user.getUsername()) != null)
            return;

        UserEntity entity = UserEntity.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        repository.save(entity);
    }

    @Override
    public void delete(int userId) {
        repository.deleteById(userId);
    }

    @Override
    public Set<User> getAllUsers() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(userEntity -> User.builder()
                        .first_name(userEntity.getFirstName())
                        .last_name(userEntity.getLastName())
                        .id(userEntity.getId())
                        .password(userEntity.getPassword())
                        .build()).collect(Collectors.toSet());
    }

    @Override
    public User findUserByToken(UUID token) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getToken() != null && userEntity.getToken().equals(token))
                .findAny()
                .orElse(null);
        return userEntityToUser(userEnt);
    }

    @Override
    public User tryLogin(String username, String password) {
        return userEntityToUser(StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getUsername().equals(username)
                        && userEntity.getPassword().equals(password))
                .findAny().orElse(null));
    }

    @Override
    public UserEntity findUserEntByToken(UUID token) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getToken() != null && userEntity.getToken().equals(token))
                .findAny()
                .orElse(null);
        return userEnt;
    }

    @Override
    public UUID handleLogin(User user) {
        UserEntity userEntity = repository.findById(user.getId()).get();
        UUID token = UUID.randomUUID();
        userEntity.setToken(token);
        userEntity.setLoggedIn(true);
        repository.save(userEntity);
        return token;
    }

    @Override
    public void logout(User user) {
        UserEntity userEntity = repository.findById(user.getId()).get();
        userEntity.setLoggedIn(false);
        userEntity.setToken(null);
        repository.save(userEntity);
    }

    @Override
    public User findUserByUsername(String username) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getUsername().equals(username))
                .findAny()
                .orElse(null);

        return userEntityToUser(userEnt);
    }

    @Override
    public List<ProductResponse> getUsersProducts(UUID token) {

        UserEntity userEntity = findUserEntByToken(token);
        if (userEntity == null)
            return null;
        return userEntity.getOrders().stream().flatMap(orderEntity -> orderEntity.getProducts().stream())
                .map(this::productEntToProductResp)
                .collect(Collectors.toList());
    }

    @Override
    public void addOrder(UUID token) {
        UserEntity userEntity = findUserEntByToken(token);
        if (userEntity == null)
            return;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(userEntity);
        orderEntity.setOrderDate(new Date());
        userEntity.getOrders().add(orderEntity);
        repository.save(userEntity);

    }

    @Override
    public void addProduct(ProductRequest productRequest, UUID token) {
        UserEntity user = findUserEntByToken(token);
        if (user == null)
            return;
        Optional<OrderEntity> foundOrder = user.getOrders().stream().filter(orderEntity ->
                orderEntity.getId() == productRequest.getOrderId()).findAny();
        if (foundOrder.isEmpty())
            return;
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productRequest.getProductName());
        productEntity.setProductPrice(productRequest.getProductPrice());
        productEntity.setOrder(foundOrder.get());
        foundOrder.get().getProducts().add(productEntity);
        repository.save(user);
    }

    @Override
    public List<ProductResponse> getAllProducts(UUID token) {
        UserEntity userEntity = findUserEntByToken(token);
        return userEntity.getOrders().stream().flatMap(orderEntity -> orderEntity.getProducts().stream())
                .map(this::productEntToProductResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getUsersOrders(UUID token) {
        UserEntity user = findUserEntByToken(token);
        if (user == null)
            return null;
        return user.getOrders().stream().map(orderEntity ->
                new OrderResponse(orderEntity.getId(), orderEntity.
                        getProducts()
                        .stream()
                        .map(this::productEntToProductResp)
                        .collect(Collectors.toList()), orderEntity.getOrderDate()))
                .collect(Collectors.toList());
    }

    private User userEntityToUser(UserEntity userEntity) {
        if (userEntity == null)
            return null;
        return User.builder()
                .first_name(userEntity.getFirstName())
                .last_name(userEntity.getLastName())
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .token(userEntity.getToken())
                .build();
    }

    private ProductResponse productEntToProductResp(ProductEntity productEntity) {
        return new ProductResponse(productEntity.getId(), productEntity.getProductName(),
                productEntity.getProductPrice(), productEntity.getOrder().getId());
    }

}
