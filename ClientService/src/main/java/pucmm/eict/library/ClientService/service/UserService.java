package pucmm.eict.library.ClientService.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pucmm.eict.library.ClientService.dto.CartItemDTO;
import pucmm.eict.library.ClientService.dto.UserDTO;
import pucmm.eict.library.ClientService.dto.UserResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UserDTO> getUsers() {
        String url = apiGatewayUrl + "/users";
        return List.of(Objects.requireNonNull(restTemplate.getForObject(url, UserDTO[].class)));
    }

    public UserResponse getCurrentUser(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(apiGatewayUrl + "/users/current-user", HttpMethod.GET, entity, UserResponse.class);
        return response.getBody();
    }

    public UserDTO changeUserRole(String id) {
        String url = apiGatewayUrl + "/users/" + id + "/change-role";
        return restTemplate.getForObject(url, UserDTO.class);
    }

    public UserDTO getUserById(String id) {
        String url = apiGatewayUrl + "/users/id/" + id;
        UserDTO user = restTemplate.getForObject(url, UserDTO.class);
        System.out.println(user);
        return user;
    }

    public UserDTO getUserByUsername(String username) {
        String url = apiGatewayUrl + "/users/" + username;
        UserDTO user = restTemplate.getForObject(url, UserDTO.class);
        System.out.println(user);
        return user;
    }

    public void removeUser(String id) {
        String url = apiGatewayUrl + "/users/" + id;
        restTemplate.delete(url);
    }

    public UserDTO createUser(UserDTO userDTO) {
        String url = apiGatewayUrl + "/auth/register";
        return restTemplate.postForObject(url, userDTO, UserDTO.class);
    }
}
