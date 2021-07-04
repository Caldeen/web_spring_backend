package pl.edu.wat.backend.entities;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<ProductEntity> products;
    private Date orderDate;
    @ManyToOne
    private UserEntity user;
}
