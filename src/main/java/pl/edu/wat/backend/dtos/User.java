package pl.edu.wat.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private int id;
    private String username;
    private String first_name;
    private String last_name;
    private String password;
    private UUID token;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
