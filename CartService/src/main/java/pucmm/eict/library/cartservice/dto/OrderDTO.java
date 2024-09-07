package pucmm.eict.library.cartservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pucmm.eict.library.cartservice.model.CartItem;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Cart items cannot be null")
    @Size(min = 1, message = "At least one cart item must be provided")
    private List<CartItem> cartItems;

    private double totalPrice;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String billingAddress;
    private String deliveryMethod;
    private String status;
    private LocalDateTime createDate;
}
