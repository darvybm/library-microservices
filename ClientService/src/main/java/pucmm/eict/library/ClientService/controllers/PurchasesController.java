package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.ClientService.dto.*;
import pucmm.eict.library.ClientService.service.CartService;
import pucmm.eict.library.ClientService.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/purchases")
public class PurchasesController {

    private OrderService orderService;
    private final CartService cartService;

    @Autowired
    public PurchasesController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }


    @GetMapping("")
    public String getPurchases(Model model, HttpServletRequest request) {
        UserResponse currentUser = (UserResponse) request.getAttribute("user");

        List<Order> orders =  orderService.getOrdersByUserId(currentUser.getId());
        model.addAttribute("orders", orders);
        return "purchases/list";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("orderDTO") Order orderDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cartItems", cartService.getCartItems());
            model.addAttribute("totalCart", cartService.getCartItems().stream().mapToDouble(CartItemDTO::getPrice).sum());
            return "purchases/list";
        }

        try {
            Order createdOrder = orderService.createOrder(orderDTO);
            model.addAttribute("createdOrder", createdOrder);
            return "index"; // Página de confirmación
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create order: " + e.getMessage());
            return "purchases/list";
        }
    }


}
