package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.ClientService.config.UserInterceptor;
import pucmm.eict.library.ClientService.dto.Book;
import pucmm.eict.library.ClientService.dto.Review;
import pucmm.eict.library.ClientService.dto.UserResponse;
import pucmm.eict.library.ClientService.service.CatalogueService;
import pucmm.eict.library.ClientService.service.ReviewService;
import pucmm.eict.library.ClientService.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookController {

    private final UserInterceptor userInterceptor;
    private final UserService userService;
    private CatalogueService catalogueService;
    private static final Logger logger = LoggerFactory.getLogger(CatalogueService.class);


    @Autowired
    public BookController(CatalogueService catalogueService, ReviewService reviewService, UserInterceptor userInterceptor, UserService userService) {
        this.catalogueService = catalogueService;
        this.userInterceptor = userInterceptor;
        this.userService = userService;
    }

    @GetMapping("")
    public String getAllBooks(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order", required = false) String order,
            Model model
    ) {
        List<Book> books = catalogueService.getCatalogueItems();
        List<Book> filteredBooks;
        if (genre != null && !genre.isEmpty()) {
            filteredBooks = catalogueService.getBooksByGenre(genre);
        } else if (search != null && !search.isEmpty()) {
            filteredBooks = new ArrayList<>();
            filteredBooks.addAll(catalogueService.getBooksByTitle(search));
            filteredBooks.addAll(catalogueService.getBooksByAuthor(search));
        } else{
            filteredBooks = books;
        }

        // Ordenar libros
        if (order != null && !order.isEmpty()) {
            switch (order) {
                case "a-z":
                    filteredBooks.sort(Comparator.comparing(Book::getTitle));
                    break;
                case "z-a":
                    filteredBooks.sort(Comparator.comparing(Book::getTitle).reversed());
                    break;
                case "low-to-high":
                    filteredBooks.sort(Comparator.comparing(Book::getPrice));
                    break;
                case "high-to-low":
                    filteredBooks.sort(Comparator.comparing(Book::getPrice).reversed());
                    break;
            }
        }

        List<String> genres = new ArrayList<>();

        for (Book book : books) {
            for (String g : book.getGenres()) {
                if (!genres.contains(g)) {
                    genres.add(g);
                }
            }
        }

        model.addAttribute("books", filteredBooks);
        model.addAttribute("genres", genres);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("searchQuery", search);
        model.addAttribute("order", order);


        return "library/shop";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        logger.info("Request to get book with ID: {}", id);

        Book book = catalogueService.getBookById(id, request);
        if (book != null) {
            book.calculateAverageRating(); // Calcula el averageRating
        }

        assert book != null;
        List<Book> relatedBooks = catalogueService.getBooksByGenres(book.getGenres());

        model.addAttribute("book", book);
        model.addAttribute("genres", book.getGenres());
        model.addAttribute("books", relatedBooks);
        model.addAttribute("reviews", book.getReviews());
        model.addAttribute("newReview", new Review());

        logger.info("Returning view for book with ID: {}", id);
        return "library/shop-detail";
    }


    @PostMapping("/{bookId}/review")
    @ResponseBody
    public ResponseEntity<?> addReview(@PathVariable("bookId") String bookId, @ModelAttribute("newReview") Review review, BindingResult bindingResult, HttpServletRequest request) {
        UserResponse currentUser = (UserResponse) request.getAttribute("user");
        logger.info("Request to add review for book ID: {} by user: {}", bookId, currentUser.getUsername());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors while adding review for book ID: {}: {}", bookId, bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body("Validation errors");
        }

        // Validar si el usuario ha comprado el libro
        if (!catalogueService.hasUserPurchasedBook(currentUser.getId(), bookId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes dejar una reseña si no has comprado este libro.");
        }

        catalogueService.addReviewToBook(bookId, review, currentUser.getId(), request);

        logger.info("Successfully added review for book ID: {}", bookId);
        return ResponseEntity.ok().body(Map.of("bookId", bookId));
    }

}
