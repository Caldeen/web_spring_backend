package pl.edu.wat.backend.services;

import pl.edu.wat.backend.dtos.ProductRequest;
import pl.edu.wat.backend.dtos.ProductResponse;
import pl.edu.wat.backend.dtos.ProductUpdateRequest;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    void addProduct(ProductRequest productRequest);

    boolean deleteProduct(int productId);

    void updateProduct(ProductUpdateRequest productRequest);
}
