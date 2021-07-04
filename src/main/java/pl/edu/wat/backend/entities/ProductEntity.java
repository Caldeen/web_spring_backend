package pl.edu.wat.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String productName;
    private float productPrice;
    @ManyToOne
    private OrderEntity order;
    public ProductEntity(String productName, float productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }
    public ProductEntity(String productName, float productPrice,OrderEntity order) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.order = order;
    }
    public ProductEntity setOrder(OrderEntity order){
        this.order = order;
        return this;
    }
}
