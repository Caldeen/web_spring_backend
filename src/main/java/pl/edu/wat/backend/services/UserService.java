package pl.edu.wat.backend.services;

import pl.edu.wat.backend.api.User;
import pl.edu.wat.backend.jpa.UserEntity;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    void add(User user);
    void delete(int userId);
    Set<User> getAllUsers();

    User findUserByToken(UUID token);
    User tryLogin(String username, String password);
    UserEntity findUserEntByToken(UUID token);

    UUID handleLogin(User user);

    void logout(User user);

    User findUserByUsername(String username);
}
