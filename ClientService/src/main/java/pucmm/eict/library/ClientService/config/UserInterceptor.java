package pucmm.eict.library.ClientService.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pucmm.eict.library.ClientService.dto.UserResponse;
import pucmm.eict.library.ClientService.service.CartService;
import pucmm.eict.library.ClientService.service.UserService;

import java.io.IOException;


@Component
public class UserInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final CryptoConfig cryptoConfig;
    private final CartService cartService;

    public UserInterceptor(UserService userService, CryptoConfig cryptoConfig, CartService cartService) {
        this.userService = userService;
        this.cryptoConfig = cryptoConfig;
        this.cartService = cartService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String jwtToken = extractJwtFromRequest(request);

            if (jwtToken != null && !jwtToken.isEmpty()) {
                try {
                    UserResponse currentUser = userService.getCurrentUser(jwtToken);
                    int cartItemCount = cartService.getCartAvailableItemsByUserId(currentUser.getId()).size();
                    request.setAttribute("user", currentUser);
                    request.setAttribute("isLoggedIn", true);
                    request.setAttribute("cartItemCount", cartItemCount);
                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                        // Token expirado
                        invalidateJwtToken(response);
                        response.sendRedirect("/login?expired=true");
                        return false;
                    }
                    throw e;
                }
            } else {
                request.setAttribute("isLoggedIn", false);
            }
            return true;
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                System.out.println("Error en la deserialización de la respuesta: " + e);
                response.sendRedirect("/error-page");
                return false;
            }
            throw e;
        }
    }

    private void invalidateJwtToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            Boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn");
            UserResponse userDTO = (UserResponse) request.getAttribute("user");

            if (isLoggedIn != null && userDTO != null) {
                modelAndView.addObject("isLoggedIn", isLoggedIn);
                modelAndView.addObject("user", userDTO);
            } else {
                modelAndView.addObject("isLoggedIn", false);
            }
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        return viewName != null && viewName.startsWith("redirect:");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cryptoConfig.decrypt(cookie.getValue());
                }
            }
        }
        return null;
    }
}