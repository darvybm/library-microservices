package pucmm.eict.library.ClientService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pucmm.eict.library.ClientService.dto.Order;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CatalogueService.class);


    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Order> getOrders() {
        String url = apiGatewayUrl + "/orders";
        Order[] ordersArray = restTemplate.getForObject(url, Order[].class);
        assert ordersArray != null;
        return Arrays.asList(ordersArray);
    }

    public List<Order> getOrdersByUserId(String userId) {
        String url = apiGatewayUrl + "/orders/id/" + userId;
        Order[] ordersArray = restTemplate.getForObject(url, Order[].class);
        assert ordersArray != null;
        return Arrays.asList(ordersArray);
    }

    public Order getOrderById(long id){
        String url = apiGatewayUrl + "/orders/" + id;
        Order order = restTemplate.getForObject(url, Order.class);
        assert order != null;
        return order;
    }

    public Order createOrder(Order orderDTO) {
        String url = apiGatewayUrl + "/orders/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> requestEntity = new HttpEntity<>(orderDTO, headers);

        // Log the request body
        System.out.println("Request Body: " + requestEntity.getBody());

        return restTemplate.postForObject(url, requestEntity, Order.class);
    }

    public Order updateOrder(Order orderDTO) {
        String url = apiGatewayUrl + "/orders/" + orderDTO.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> requestEntity = new HttpEntity<>(orderDTO, headers);

        return restTemplate.postForObject(url, requestEntity, Order.class);
    }

    public void sendOrderToInvoice(Order orderDTO) {
        String url = apiGatewayUrl + "/reports/invoice";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> requestEntity = new HttpEntity<>(orderDTO, headers);

        try {
            logger.info("Enviando solicitud para crear la factura: " + orderDTO);
            ResponseEntity<byte[]> response = restTemplate.postForEntity(url, requestEntity, byte[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                byte[] pdfBytes = response.getBody();
                logger.info("Factura creada exitosamente.");
            } else {
                logger.error("Error al crear la factura: " + response.getStatusCode());
            }
        } catch (HttpServerErrorException e) {
            logger.error("Error del servidor al crear la factura: " + e.getResponseBodyAsString(), e);
            throw e; // Re-lanza la excepción después de registrarla
        } catch (Exception e) {
            logger.error("Error desconocido al crear la factura", e);
            throw e; // Re-lanza la excepción después de registrarla
        }
    }


}