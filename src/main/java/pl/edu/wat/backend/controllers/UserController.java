package pl.edu.wat.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.backend.services.UserService;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("SESSION") UUID token) {
        User user = userService.findUserByToken(token);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            userService.logout(user);
            return new ResponseEntity<String>("logged out", HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.add(User.builder()
                .username(registerRequest.getUsername())
                .birthDate(registerRequest.getDate())
                .password(registerRequest.getPassword())
                .first_name(registerRequest.getFirstName())
                .last_name(registerRequest.getLastName())
                .phone_number(registerRequest.getPhoneNumber())
                .build());
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRequest> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.tryLogin(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginRequest("none", "none"));
        } else {
            UUID token = userService.handleLogin(user);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("SESSION", token.toString());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders)
                    .body(new LoginRequest(loginRequest.getUsername(),
                            loginRequest.getPassword())
                    );
        }
    }
}
