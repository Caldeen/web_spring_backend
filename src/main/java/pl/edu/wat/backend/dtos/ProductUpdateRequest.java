package pl.edu.wat.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.backend.entities.OrderEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    private int id;
    private String newProductName;
    private float newProductPrice;
//    private int orderId;
}
