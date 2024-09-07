package pucmm.eict.library.ClientService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @NotNull(message = "ID cannot be null")
    private int id;

    @NotNull(message = "User ID cannot be null")
    private String userId;

    private UserDTO user;

    @NotNull(message = "Cart items cannot be null")
    @Size(min = 1, message = "At least one cart item must be provided")
    private List<CartItemDTO> cartItems;

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
