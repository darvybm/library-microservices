package pucmm.eict.library.ClientService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    private String id;
    private String title;
    private String description;
    private String author;
    private List<String> genres;
    private double price;
    private String cover;
    private Double averageRating = 0.0;
    private LocalDateTime createdAt;
    private List<Review> reviews;

    // Método para actualizar el promedio del rating
    public void calculateAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            this.averageRating = 0.0;
        } else {
            double sum = reviews.stream().mapToDouble(Review::getRating).sum();
            this.averageRating = sum / reviews.size();
        }
    }
}
