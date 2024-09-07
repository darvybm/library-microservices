package pucmm.eict.library.cartservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private List<String> genres;
    private double price;
    private String cover;
}