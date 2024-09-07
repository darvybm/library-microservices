package pucmm.eict.library.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String userId;
    private List<CartItemDTO> cartItems;
    private double totalPrice;
    private String address;
    private String paymentMethod;
    private String billingAddress;
    private String deliveryMethod;
    private String status;
    private LocalDateTime createDate;
}