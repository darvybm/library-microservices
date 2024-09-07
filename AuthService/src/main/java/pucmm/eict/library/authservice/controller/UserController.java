package pucmm.eict.library.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.authservice.dto.UserDTO;
import pucmm.eict.library.authservice.model.Role;
import pucmm.eict.library.authservice.model.User;
import pucmm.eict.library.authservice.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        System.out.println("USERS");
        List<User> users = userService.getAllUsers();
        System.out.println("USERS");
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User userOpt = userService.getUserById(id);
        return userOpt != null ? ResponseEntity.ok(userOpt) : ResponseEntity.notFound().build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/{id}/change-role")
    public ResponseEntity<?> assignRoleToUser(@PathVariable String id) {

        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String role = user.getRole() == Role.ADMIN ? Role.USER.name() : Role.ADMIN.name();
        user.setRole(Role.valueOf(role));
        User updatedUser = userService.update(user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    // This method removes the user from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(user);
        return ResponseEntity.ok().build();
    }
}
