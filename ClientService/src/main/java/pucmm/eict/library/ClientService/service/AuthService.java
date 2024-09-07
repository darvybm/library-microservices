package pucmm.eict.library.ClientService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pucmm.eict.library.ClientService.dto.*;


@Service
public class AuthService {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserResponse registerUser(RegisterDTO registerDTO) {
        String url = apiGatewayUrl + "/auth/register";
        try {
            ResponseEntity<UserResponse> response = restTemplate.postForEntity(url, registerDTO, UserResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody(); // Retorna el cuerpo de la respuesta directamente
            } else {
                // Manejo de un caso de respuesta sin cuerpo
                throw new RestClientException("Registration failed with status: " + response.getStatusCode() +
                        ", message: " + "No response body");
            }
        } catch (RestClientException e) {
            logger.error("Error registering userDTO: " + e.getMessage());
            throw e;
        }
    }

    public TokenDTO loginUser(LoginDTO loginDTO) throws RestClientException {
        String url = apiGatewayUrl + "/auth/login";
        try {
            ResponseEntity<TokenDTO> response = restTemplate.postForEntity(url, loginDTO, TokenDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RestClientException("Error de autenticación: Estado HTTP " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error en login: " + e.getMessage());
            throw new RestClientException("Error de cliente o servidor HTTP: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error del sistema: " + e.getMessage());
            throw new RuntimeException("Error del sistema en login", e);
        }
    }

}

