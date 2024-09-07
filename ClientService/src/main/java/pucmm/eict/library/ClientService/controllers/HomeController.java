package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pucmm.eict.library.ClientService.dto.Book;
import pucmm.eict.library.ClientService.dto.Order;
import pucmm.eict.library.ClientService.dto.Review;
import pucmm.eict.library.ClientService.service.CatalogueService;
import pucmm.eict.library.ClientService.service.OrderService;
import pucmm.eict.library.ClientService.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private CatalogueService catalogueService;
    private UserService userService;
    private OrderService orderService;

    public HomeController(CatalogueService catalogueService, UserService userService, OrderService orderService) {
        this.catalogueService = catalogueService;
        this.userService = userService;
        this.orderService = orderService;
    }


    @GetMapping("/")
    public String home(
            @RequestParam(value = "genre", required = false) String genre,
            Model model, @CookieValue(name = "jwt-token", required = false) String jwtToken,
            HttpServletRequest request
    ) {

        // Obteniendo la lista de libros de la BD
        List<Book> books = catalogueService.getCatalogueItems();

        // Obtener 20 libros random
        List<Book> recommendedBooks;
        if (genre != null && !genre.equals("Todos")) {
            recommendedBooks = books.stream()
                    .filter(book -> book.getGenres().contains(genre))
                    .limit(12)
                    .toList();
        } else {
            recommendedBooks = books.stream().limit(12).toList();
        }

        // Obtener los libros más recientes
        List<Book> newBooks = books.stream().sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(8)
                .toList();

        // Obtener los 10 libros con mejor rating
        List<Book> booksBestseller = books.stream().sorted((b1, b2) -> b2.getAverageRating().compareTo(b1.getAverageRating()))
                .limit(6)
                .toList();

        // Obtener los 10 libros con mejor rating
        List<Book> booksBestsellerP2 = books.stream().sorted((b1, b2) -> b2.getAverageRating().compareTo(b1.getAverageRating()))
                .skip(6)
                .limit(4)
                .toList();

        // Obteniendo todos los generos
        List<String> genres = books.stream()
                .map(Book::getGenres)
                .flatMap(List::stream)
                .distinct()
                .limit(9)
                .toList();


        for (Book book : booksBestseller) {
            List<Review> reviews = catalogueService.getReviewsByBookId(book.getId(), request);
            book.setReviews(reviews);
            book.calculateAverageRating();
        }

        for (Book book : booksBestsellerP2) {
            List<Review> reviews = catalogueService.getReviewsByBookId(book.getId(), request);
            book.setReviews(reviews);
            book.calculateAverageRating();
        }

        model.addAttribute("books", recommendedBooks);
        model.addAttribute("newBooks", newBooks);
        model.addAttribute("booksBestseller", booksBestseller);
        model.addAttribute("booksBestsellerP2", booksBestsellerP2);
        model.addAttribute("genres", genres);
        model.addAttribute("selectedGenre", genre != null ? genre : "Todos");

        // Contadores
        List<Order> orders = orderService.getOrders();
        Set<String> genresAvailable = books.stream()
                .flatMap(book -> book.getGenres().stream())
                .collect(Collectors.toSet());

        model.addAttribute("ordersQuantity", orders.size());
        model.addAttribute("booksQuantity", books.size());
        model.addAttribute("genresQuantity", genresAvailable.size());



        return "index";
    }

}
