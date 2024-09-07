package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.ClientService.dto.CartItemDTO;
import pucmm.eict.library.ClientService.dto.Order;
import pucmm.eict.library.ClientService.dto.UserResponse;
import pucmm.eict.library.ClientService.dto.request.CartItemResponse;
import pucmm.eict.library.ClientService.service.CartService;
import pucmm.eict.library.ClientService.service.CatalogueService;
import pucmm.eict.library.ClientService.service.OrderService;
import pucmm.eict.library.ClientService.util.UserUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pucmm.eict.library.ClientService.dto.Book;


@Controller
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private CatalogueService catalogueService;
    private OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);


    public CartController(CartService cartService, CatalogueService catalogueService, OrderService orderService) {
        this.cartService = cartService;
        this.catalogueService = catalogueService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String viewCart(Model model, HttpServletRequest request) {
        processCartItems(model, request);

        return "cart/cart";
    }

    @PostMapping("/add")
    public String addCartItem(@RequestParam("bookId") String bookId, @RequestParam("quantity") Integer quantity, HttpServletRequest request) {
        UserResponse currentUser = UserUtils.getCurrentUser(request);
        if (currentUser != null) {
            CartItemResponse cartItemResponse = CartItemResponse.builder()
                    .bookId(bookId)
                    .quantity(quantity)
                    .userId(currentUser.getId())
                    .build();

            cartService.addToCart(cartItemResponse);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("id") Long id, HttpServletRequest request) {
        UserResponse currentUser = UserUtils.getCurrentUser(request);
        if (currentUser != null) {
            cartService.removeFromCart(id);
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutCart(Model model, HttpServletRequest request) {
        processCartItems(model, request);
        UserResponse currentUser = UserUtils.getCurrentUser(request);

        // Agrega cualquier lógica necesaria para inicializar el formulario
        Order order = new Order();
        order.setPaymentMethod("Paypal");
        model.addAttribute("orderDTO", order);
        model.addAttribute("userId", currentUser.getId());
        return "cart/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @Valid @ModelAttribute("orderDTO") Order orderDTO,
            BindingResult result, Model model, HttpServletRequest request
    ) {
        logger.info("Iniciando el proceso de checkout.");

        if (result.hasErrors()) {
            logger.warn("Errores de validación encontrados: " + result.getAllErrors());
            processCartItems(model, request);
            return "cart/checkout";
        }

        // Obtener el usuario actual y los items del carrito
        UserResponse currentUser = (UserResponse) request.getAttribute("user");
        if (currentUser != null) {
            String userId = currentUser.getId();
            List<CartItemDTO> cartItems = cartService.getCartAvailableItemsByUserId(userId);

            double totalPrice = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

            orderDTO.setUserId(userId);
            orderDTO.setTotalPrice(totalPrice);
            orderDTO.setStatus("COMPLETED");
            orderDTO.setCreateDate(LocalDateTime.now());
            orderDTO.setCartItems(cartItems);

            try {
                Order createdOrder = orderService.createOrder(orderDTO);
                model.addAttribute("order", createdOrder);
                logger.info("Orden creada exitosamente: " + createdOrder);

                model.addAttribute("amount", createdOrder.getTotalPrice());
                model.addAttribute("orderId", createdOrder.getId());

                // Enviar la orden a la facturación
                orderService.sendOrderToInvoice(createdOrder);
                logger.info("Orden enviada exitosamente para facturación.");

                return "cart/redirectPost";
            } catch (Exception e) {
                logger.error("Error al crear la orden", e);
                model.addAttribute("errorMessage", "Error al crear la orden: " + e.getMessage());
                processCartItems(model, request);
                return "cart/checkout";
            }
        } else {
            logger.warn("Usuario no encontrado en la solicitud.");
            return "redirect:/cart";
        }
    }





    private void processCartItems(Model model, HttpServletRequest request) {
        UserResponse currentUser = UserUtils.getCurrentUser(request);
        if (currentUser != null) {
            String userId = currentUser.getId();
            List<CartItemDTO> cartItems = cartService.getCartAvailableItemsByUserId(userId);

            List<CartItemDTO> detailedCartItems = cartItems.stream()
                    .peek(cartItem -> {
                        Book book = catalogueService.getBookById(cartItem.getBookId(), request);
                        if (book != null) {
                            cartItem.setBook(book);
                            cartItem.setPrice(book.getPrice());
                        } else {
                            cartItem.setPrice(0.0);
                        }
                    }).collect(Collectors.toList());

            double totalCart = detailedCartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
            model.addAttribute("cartItems", detailedCartItems);
            model.addAttribute("totalCart", totalCart);
        } else {
            model.addAttribute("cartItems", List.of());
            model.addAttribute("totalCart", 0);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCartItemQuantity(@RequestParam Long cartItemId, @RequestParam int quantity, @RequestParam String action) {
        if ("increment".equals(action)) {
            // Aquí no incrementamos nuevamente, solo actualizamos con la nueva cantidad
            cartService.updateCartItemQuantity(cartItemId, quantity);
        } else if ("decrement".equals(action)) {
            // Aquí no decrementamos nuevamente, solo actualizamos con la nueva cantidad
            cartService.updateCartItemQuantity(cartItemId, quantity);
        } else if ("remove".equals(action)) {
            cartService.removeFromCart(cartItemId);
            quantity = 0;
        } else {
            cartService.updateCartItemQuantity(cartItemId, quantity);
        }

        // Obtener el nuevo conteo de ítems en el carrito
        int cartItemCount = cartService.getCartItems().size();

        // Crear respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("cartItemCount", cartItemCount);
        response.put("message", "Quantity updated successfully");

        return response;
    }

}
