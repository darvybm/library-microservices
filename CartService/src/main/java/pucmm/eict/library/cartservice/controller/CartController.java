package pucmm.eict.library.cartservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.cartservice.dto.BookDTO;
import pucmm.eict.library.cartservice.model.CartItem;
import pucmm.eict.library.cartservice.service.CartService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("")
    public ResponseEntity<List<CartItem>> getCartItems() {
        List<CartItem> cartItems = cartService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItem cartItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Construir mensajes de error detallados
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        CartItem addedCartItem = cartService.addToCart(cartItem);
        return ResponseEntity.ok(addedCartItem);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String userId) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{userId}/available")
    public ResponseEntity<List<CartItem>> getAvailableCartItems(@PathVariable String userId) { // Nuevo endpoint
        List<CartItem> cartItems = cartService.getCartItemsByUserIdAndOrderIsNull(userId);
        return ResponseEntity.ok(cartItems);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable Long id, @RequestParam int quantity) {
        try {
            CartItem updatedCartItem = cartService.updateCartItemQuantity(id, quantity);
            return ResponseEntity.ok(updatedCartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating cart item quantity: " + e.getMessage());
        }
    }

}