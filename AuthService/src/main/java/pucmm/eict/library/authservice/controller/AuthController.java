package pucmm.eict.library.authservice.controller;

import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.authservice.client.NotificationClient;
import pucmm.eict.library.authservice.dto.LoginDTO;
import pucmm.eict.library.authservice.dto.MailRegistrationDTO;
import pucmm.eict.library.authservice.dto.TokenDTO;
import pucmm.eict.library.authservice.dto.UserDTO;
import pucmm.eict.library.authservice.model.Role;
import pucmm.eict.library.authservice.model.User;
import pucmm.eict.library.authservice.service.AuthService;
import pucmm.eict.library.authservice.service.JwtService;
import pucmm.eict.library.authservice.service.UserService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthService authService;
    private final NotificationClient notificationClient;

    @Autowired
    public AuthController(JwtService jwtService, AuthService authService, UserService userService, NotificationClient notificationClient) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.notificationClient = notificationClient;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            if (!isValidRole(userDTO.getRole())) {
                return ResponseEntity.badRequest().body("Invalid role");
            }
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            User registeredUser = authService.registerUser(user);

            MailRegistrationDTO mailRegistrationDTO = MailRegistrationDTO.builder()
                    .recipient(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .subject("Welcome to the library")
                    .build();

            notificationClient.sendMail(mailRegistrationDTO);

            return ResponseEntity.ok(registeredUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDTO loginRequest) {
        try {
            System.out.println("Attempting to authenticate user");

            User authenticateUser = authService.autheticateUser(loginRequest.getUsername(), loginRequest.getPassword());

            if (authenticateUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String jwtToken = jwtService.generateToken(authenticateUser);
            TokenDTO tokenDTO = TokenDTO.builder()
                    .token(jwtToken)
                    .expiration(jwtService.getExpirationTime())
                    .build();

            return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }

    private boolean isValidRole(Role role) {
        List<Role> validRoles = Arrays.stream(Role.values()).toList();
        return validRoles.contains(role);
    }

    @GetMapping("/isValidToken")
    public ResponseEntity<?> isValidToken(@RequestParam String token) {
        return ResponseEntity.ok(jwtService.isValidToken(token));
    }

}
