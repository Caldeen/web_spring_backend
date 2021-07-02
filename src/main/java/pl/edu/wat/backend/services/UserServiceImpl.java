package pl.edu.wat.backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.stereotype.Service;
import pl.edu.wat.backend.api.User;
import pl.edu.wat.backend.jpa.UserEntity;
import pl.edu.wat.backend.repositories.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Override
    public void add(User user) {
        if(findUserByUsername(user.getUsername())!=null)
            return;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = user.getBirthDate();
        Date dateToAdd = null;
        if(!date.equals("--"))
        {
            try {
                dateToAdd = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(dateToAdd);
        UserEntity entity = UserEntity.builder()
                .birthDate(dateToAdd)
                .username(user.getUsername())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .phoneNumber(user.getPhone_number())
                .password(user.getPassword())
                .build();
        repository.save(entity);
    }

    @Override
    public void delete(int userId) {
        repository.deleteById(userId);
    }

    @Override
    public Set<User> getAllUsers() {
        return StreamSupport.stream(repository.findAll().spliterator(),false)
                .map(userEntity -> User.builder()
                .first_name(userEntity.getFirstName())
                .last_name(userEntity.getLastName())
                .email(userEntity.getEmail())
                .id(userEntity.getId())
                .phone_number(userEntity.getPhoneNumber())
                .password(userEntity.getPassword())
                .build()).collect(Collectors.toSet());
    }

    @Override
    public User findUserByToken(UUID token) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity ->userEntity.getToken()!=null&& userEntity.getToken().equals(token))
                .findAny()
                .orElse(null);
        return userEntityToUser(userEnt);
    }

    @Override
    public User tryLogin(String username, String password) {
        return userEntityToUser(StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getUsername().equals(username)
                        && userEntity.getPassword().equals(password))
                .findAny().orElse(null));
    }

    @Override
    public UserEntity findUserEntByToken(UUID token) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity ->userEntity.getToken()!=null&& userEntity.getToken().equals(token))
                .findAny()
                .orElse(null);
        return userEnt;
    }

    @Override
    public UUID handleLogin(User user) {
        UserEntity userEntity = repository.findById(user.getId()).get();
        UUID token = UUID.randomUUID();
        userEntity.setToken(token);
        userEntity.setLoggedIn(true);
        repository.save(userEntity);
        return token;
    }

    @Override
    public void logout(User user) {
        UserEntity userEntity = repository.findById(user.getId()).get();
        userEntity.setLoggedIn(false);
        userEntity.setToken(null);
        repository.save(userEntity);
    }
    @Override
    public User findUserByUsername(String username) {
        UserEntity userEnt = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(userEntity -> userEntity.getUsername().equals(username))
                .findAny()
                .orElse(null);

        return userEntityToUser(userEnt);
    }
    private User userEntityToUser(UserEntity userEntity) {
        if (userEntity == null)
            return null;
        return User.builder()
                .first_name(userEntity.getFirstName())
                .last_name(userEntity.getLastName())
                .email(userEntity.getEmail())
                .id(userEntity.getId())
                .phone_number(userEntity.getPhoneNumber())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .meetings(userEntity.getMeetings())
                .token(userEntity.getToken())
                .usersInvitations(userEntity.getUsersInvitations())
                .build();
    }

}
