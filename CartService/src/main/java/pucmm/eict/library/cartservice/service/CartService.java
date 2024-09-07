package pucmm.eict.library.cartservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pucmm.eict.library.cartservice.client.CatalogueClient;
import pucmm.eict.library.cartservice.dto.BookDTO;
import pucmm.eict.library.cartservice.model.CartItem;
import pucmm.eict.library.cartservice.repository.CartRepository;

import java.awt.print.Book;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    private final CatalogueClient catalogueClient;

    @Autowired
    public CartService(CartRepository cartRepository, CatalogueClient catalogueClient) {
        this.cartRepository = cartRepository;
        this.catalogueClient = catalogueClient;
    }

    public List<CartItem> getCartItemsByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public void clearCart(String userId) {
        logger.info("Iniciando la limpieza del carrito para el usuario: {}", userId);
        List<CartItem> cartItems = cartRepository.findByUserIdAndOrderIsNull(userId);
        cartRepository.deleteAll(cartItems);
        logger.info("Carrito limpiado exitosamente para el usuario: {}", userId);
    }


    public List<CartItem> getAllCartItems() {
        return cartRepository.findAll();
    }

    public CartItem updateCartItemQuantity(Long id, int quantity) {
        CartItem cartItem = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }

    public List<CartItem> getCartItemsByUserIdAndOrderIsNull(String userId) { // Nuevo método
        return cartRepository.findByUserIdAndOrderIsNull(userId);
    }
}