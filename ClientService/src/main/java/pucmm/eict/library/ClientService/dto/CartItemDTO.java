package pucmm.eict.library.ClientService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;
    private String bookId;
    private int quantity;
    private double price;
    private Book book;
    private String userId;
    private Order order;
}
