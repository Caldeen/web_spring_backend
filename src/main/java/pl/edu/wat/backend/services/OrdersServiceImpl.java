package pl.edu.wat.backend.services;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.wat.backend.dtos.*;
import pl.edu.wat.backend.entities.OrderEntity;
import pl.edu.wat.backend.entities.ProductEntity;
import pl.edu.wat.backend.repositories.OrderRepository;
import pl.edu.wat.backend.repositories.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrdersServiceImpl implements OrdersService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Autowired
    private OrdersServiceImpl(OrderRepository orderRepository, ProductRepository productRepository){

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    @Override
    public List<OrderResponse> getAllOrders(){
                return StreamSupport.stream(orderRepository.findAll().spliterator(),false)
                .map(a-> new OrderResponse(a.getId(),a.getProducts()
                        .stream().map(productEntity -> new ProductResponse(productEntity.getId(),
                                productEntity.getProductName(),productEntity.getProductPrice(),productEntity.getOrder().getId()))
                        .collect(Collectors.toList()),a.getOrderDate()))
                        .collect(Collectors.toList());
    }

    @Override
    public void addOrder(OrderRequest orderRequest) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderDate(orderRequest.getOrderDate());
        orderRepository.save(orderEntity);
    }

    @Override
    public boolean deleteOrder(int orderId) {
        OrderEntity orderEntity = StreamSupport.stream(orderRepository.findAll()
                .spliterator(), false)
                .filter(order -> order.getId()==orderId)
                .findAny()
                .orElse(null);
            if(orderEntity ==null)
                return false;
            else{
                orderRepository.delete(orderEntity);
                return true;
            }
    }

    @Override
    public void updateOrder(OrderUpdateRequest orderUpdateRequest) {
        OrderEntity orderEntity = StreamSupport.stream(orderRepository.findAll()
                .spliterator(), false)
                .filter(order -> order.getId()==orderUpdateRequest.getId())
                .findFirst()
                .orElse(null);
        try{
            orderEntity.setOrderDate(orderUpdateRequest.getNeworderDate());
            orderEntity.setProducts(getProductEntities(new OrderRequest(orderUpdateRequest.getNewproducts(),
                    orderUpdateRequest.getNeworderDate()),orderEntity));
            orderRepository.save(orderEntity);
        }catch (NullPointerException e){
        }
    }

    private List<ProductEntity> getProductEntities(OrderRequest orderRequest,OrderEntity order){
        Map<String,ProductEntity> map1 = new HashMap<>();
        List<ProductEntity> productEntities= orderRequest.getProducts().stream()
                .map(productRequest -> StreamSupport.stream(productRepository.findAll().spliterator(),false)
                .filter(productEntity -> productEntity.getProductName().equals(productRequest.getProductName()))
                        .findAny()
                        .orElseGet(()->getIfNewName(map1,productRequest,order)))
                        .collect(Collectors.toList());
        return productEntities;
    }
    private ProductEntity getIfNewName(Map<String,ProductEntity> map, ProductRequest productRequest,OrderEntity order){
        map.putIfAbsent(productRequest.getProductName(),new ProductEntity(productRequest.getProductName(),
                productRequest.getProductPrice()).setOrder(order));
        return productRepository.save(map.get(productRequest.getProductName()));
    }
}
