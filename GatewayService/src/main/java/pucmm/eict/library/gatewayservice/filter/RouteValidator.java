package pucmm.eict.library.gatewayservice.filter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openRoutes = List.of(
            "/",
            "/auth/login",
            "/auth/register",
            "/catalogue",
            "/catalogue/search/title",
            "/catalogue/search/author",
            "/catalogue/search/genre",
            "/catalogue/{id}",
            "/reviews",
            "/reviews/book/{bookId}",
            "/reviews/user/{userId}"
            );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openRoutes
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
