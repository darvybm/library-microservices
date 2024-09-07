package pucmm.eict.library.ClientService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pucmm.eict.library.ClientService.dto.CartItemDTO;
import pucmm.eict.library.ClientService.dto.request.CartItemResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartService {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;

    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CartItemDTO> getCartItems() {
        String url = apiGatewayUrl + "/cart";
        CartItemDTO[] cartItems = restTemplate.getForObject(url, CartItemDTO[].class);
        if (cartItems == null) return new ArrayList<>();
        return Arrays.asList(cartItems);
    }

    public List<CartItemDTO> getCartAvailableItemsByUserId(String userId) {
        String url = apiGatewayUrl + "/cart/" + userId + "/available";
        CartItemDTO[] cartItems = restTemplate.getForObject(url, CartItemDTO[].class);
        if (cartItems == null) return new ArrayList<>();
        return Arrays.asList(cartItems);
    }

    public CartItemResponse addToCart(CartItemResponse cartItemResponse) {
        String url = apiGatewayUrl + "/cart";
        return restTemplate.postForObject(url, cartItemResponse, CartItemResponse.class);
    }

    public void removeFromCart(Long id) {
        String url = apiGatewayUrl + "/cart/" + id;
        restTemplate.delete(url);
    }

    public void updateCartItemQuantity(Long id, int quantity) {
        String url = apiGatewayUrl + "/cart/" + id + "/quantity?quantity=" + quantity;
        restTemplate.put(url, null);
    }
}
