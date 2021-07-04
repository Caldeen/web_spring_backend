package pl.edu.wat.backend.services;

import org.springframework.stereotype.Service;
import pl.edu.wat.backend.dtos.ProductRequest;
import pl.edu.wat.backend.dtos.ProductResponse;
import pl.edu.wat.backend.dtos.ProductUpdateRequest;
import pl.edu.wat.backend.entities.ProductEntity;
import pl.edu.wat.backend.repositories.OrderRepository;
import pl.edu.wat.backend.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductsServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    public ProductsServiceImpl(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;

        this.orderRepository = orderRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(a -> new ProductResponse(a.getId(),a.getProductName(),
                        a.getProductPrice(),a.getOrder().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void addProduct(ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productRequest.getProductName());
        productEntity.setProductPrice(productRequest.getProductPrice());
        productRepository.save(productEntity);
    }

    @Override
    public boolean deleteProduct(int productId) {
        ProductEntity productEntity = StreamSupport.stream(productRepository.findAll()
                .spliterator(), false)
                .filter(product -> product.getId()==productId)
                .findFirst()
                .orElse(null);
        if(productEntity==null)
            return false;
        try{
            productRepository.delete(productEntity);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public void updateProduct(ProductUpdateRequest productRequest) {
        ProductEntity productEntity = StreamSupport.stream(productRepository.findAll()
                .spliterator(), false)
                .filter(product -> product.getId()==productRequest.getId())
                .findFirst()
                .orElse(null);
        try {
            productEntity.setProductName(productRequest.getNewProductName());
            productEntity.setProductPrice(productRequest.getNewProductPrice());
//            productEntity.setOrder(orderRepository.findById(productRequest.getOrderId()).get());
            productRepository.save(productEntity);
        }catch (NullPointerException e){
        }
    }
}
