package pucmm.eict.library.authservice.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    MODERATOR;

    public static List<Role> getAllRoles() {
        return List.of(values()); // Return an unmodifiable list of all enum values
    }

    @Override
    public String getAuthority() {
        return name();
    }
}