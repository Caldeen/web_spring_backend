package pl.edu.wat.backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductRequest {
    private String productName;
    private int productQuantity;

}
