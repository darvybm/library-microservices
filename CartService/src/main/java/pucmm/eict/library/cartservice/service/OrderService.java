package pucmm.eict.library.cartservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pucmm.eict.library.cartservice.client.CatalogueClient;
import pucmm.eict.library.cartservice.controller.CartController;
import pucmm.eict.library.cartservice.dto.BookDTO;
import pucmm.eict.library.cartservice.dto.OrderDTO;
import pucmm.eict.library.cartservice.model.CartItem;
import pucmm.eict.library.cartservice.model.Order;
import pucmm.eict.library.cartservice.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    private final CatalogueClient catalogueClient;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, CatalogueClient catalogueClient) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.catalogueClient = catalogueClient;
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        double totalPrice = 0;

        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setAddress(orderDTO.getAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setBillingAddress(orderDTO.getBillingAddress());
        order.setDeliveryMethod(orderDTO.getDeliveryMethod());
        order.setStatus("COMPLETED");
        order.setCreateDate(LocalDateTime.now());


        for (CartItem cartItemDTO : orderDTO.getCartItems()) {
            BookDTO bookDTO = catalogueClient.getBook(cartItemDTO.getBookId());
            totalPrice += cartItemDTO.getQuantity() * bookDTO.getPrice();

            CartItem cartItem = new CartItem();
            cartItem.setBookId(cartItemDTO.getBookId());
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setUserId(cartItemDTO.getUserId());
            cartItem.setOrder(order);

            // Añadir el CartItem a la lista de CartItems de la orden
            order.getCartItems().add(cartItem);
        }

        order.setTotalPrice(totalPrice);

        order = orderRepository.save(order);

        // Limpiar el carrito después de crear la orden
        logger.info("Limpiando el carrito para el usuario: {}", orderDTO.getUserId());
        cartService.clearCart(orderDTO.getUserId());

        return order;
    }



    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Optional<Order> updateOrder(Long id, Order orderDetails) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setUserId(orderDetails.getUserId());
            updatedOrder.setCartItems(orderDetails.getCartItems());
            updatedOrder.setTotalPrice(orderDetails.getTotalPrice());
            updatedOrder.setAddress(orderDetails.getAddress());
            updatedOrder.setPaymentMethod(orderDetails.getPaymentMethod());
            updatedOrder.setBillingAddress(orderDetails.getBillingAddress());
            updatedOrder.setDeliveryMethod(orderDetails.getDeliveryMethod());
            updatedOrder.setStatus(orderDetails.getStatus());
            updatedOrder.setCreateDate(orderDetails.getCreateDate());

            orderRepository.save(updatedOrder);
        }

        return order;
    }

    public boolean deleteOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            orderRepository.delete(order.get());
            return true;
        }

        return false;
    }
}
