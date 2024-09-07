package pucmm.eict.library.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemReportDTO {
    private String title;
    private String author;
    private double price;
    private int quantity;
    private Double totalPrice;
}
