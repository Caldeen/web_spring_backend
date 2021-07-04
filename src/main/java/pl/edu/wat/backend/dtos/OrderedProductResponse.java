package pl.edu.wat.backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductResponse {
    private int id;
    private String productName;
    private int productQuantity;

}
