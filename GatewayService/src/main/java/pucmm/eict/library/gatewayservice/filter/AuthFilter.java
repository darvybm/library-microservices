package pucmm.eict.library.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import pucmm.eict.library.gatewayservice.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    private final Map<String, String> routeRoleMap = new HashMap<>();

    public AuthFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;

        routeRoleMap.put("/admin", "ADMIN");
        routeRoleMap.put("/users", "ADMIN");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                    String role = jwtUtil.extractRole(authHeader);

                    if (!isValidRoleForRoute(role, exchange.getRequest().getURI().getPath())) {
                        return onError(exchange, "Unauthorized access to this resource", HttpStatus.FORBIDDEN);
                    }

                } catch (Exception e) {
                    return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    private boolean isValidRoleForRoute(String role, String path) {
        if (routeRoleMap.containsKey(path)) {
            String requiredRole = routeRoleMap.get(path);
            return role.equals(requiredRole);
        }
        return true;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {

    }
}
