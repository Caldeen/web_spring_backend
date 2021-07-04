package pl.edu.wat.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UUID token;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<OrderEntity> orders;
    private Boolean loggedIn ;

}
