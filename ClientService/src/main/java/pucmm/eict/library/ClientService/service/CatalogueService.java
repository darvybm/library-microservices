package pucmm.eict.library.ClientService.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pucmm.eict.library.ClientService.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CatalogueService {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CatalogueService.class);


    public CatalogueService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> getCatalogueItems() {
        String url = apiGatewayUrl + "/catalogue";
        Book[] books = restTemplate.getForObject(url, Book[].class);
        if (books == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(books);
    }

    public Book getBookById(String bookId, HttpServletRequest request) {
        String url = apiGatewayUrl + "/catalogue/" + bookId;
        Book book = restTemplate.getForObject(url, Book.class);

        if (book != null) {
            book.setReviews(getReviewsByBookId(bookId, request));
        }
        return book;
    }


    public List<Book> getBooksByTitle(String title) {
        String url = apiGatewayUrl + "/catalogue/search/title?title=" + title;
        Book[] books = restTemplate.getForObject(url, Book[].class);
        if (books == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(books);
    }

    public List<Book> getBooksByAuthor(String author) {
        String url = apiGatewayUrl + "/catalogue/search/author?author=" + author;
        Book[] books = restTemplate.getForObject(url, Book[].class);
        if (books == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(books);
    }

    public List<Book> getBooksByGenre(String genre) {
        String url = apiGatewayUrl + "/catalogue/search/genre?genre=" + genre;
        Book[] books = restTemplate.getForObject(url, Book[].class);
        if (books == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(books);
    }

    public List<Book> getBooksByGenres(List<String> genres) {
        List<Book> relatedBooks = new ArrayList<>();
        for (String genre : genres) {
            String url = apiGatewayUrl + "/catalogue/search/genre?genre=" + genre;
            Book[] books = restTemplate.getForObject(url, Book[].class);
            if (books != null) {
                relatedBooks.addAll(Arrays.asList(books));
            }
        }
        return relatedBooks;
    }

    public Review createReview(Review review) {
        String url = apiGatewayUrl + "/reviews";
        logger.info("Creating review for book ID: {} by user ID: {}", review.getBookId(), review.getUserId());
        logger.info("Review details: {}", review); // Log details of the review being created
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Review> request = new HttpEntity<>(review, headers);
        try {
            ResponseEntity<Review> response = restTemplate.postForEntity(url, request, Review.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully created review with ID: {}", review.getId());
                return response.getBody();
            } else {
                logger.error("Error creating review: {}", response.getStatusCode());
                throw new RuntimeException("Error creating review: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error while creating review: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public void addReviewToBook(String bookId, Review review, String userId, HttpServletRequest request) {
        review.setBookId(bookId);
        review.setUserId(userId);
        // Obtener el username del usuario autenticado y asignarlo a la review
        String userUrl = apiGatewayUrl + "/users/id/" + userId;
        logger.info("Fetching user with ID: {} from URL: {}", userId, userUrl);
        try {
            ResponseEntity<UserDTO> userResponse = restTemplate.getForEntity(userUrl, UserDTO.class);
            UserDTO user = userResponse.getBody();
            if (user != null) {
                review.setUsername(user.getUsername());
                logger.info("Set username '{}' for user ID: {}", user.getUsername(), userId);
            }
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching user with ID: {}. Setting username to 'Unknown User'.", userId, e);
            review.setUsername("Unknown User");
        }

        // Crear review
        createReview(review);

        // Obtener el libro actual para recalcular el promedio
        Book book = getBookById(bookId, request); // Implementa este método para obtener el libro por ID
        if (book != null) {
            // Asegúrate de que la lista de reseñas sea mutable
            if (book.getReviews() == null) {
                book.setReviews(new ArrayList<>());
            } else if (!(book.getReviews() instanceof ArrayList)) {
                book.setReviews(new ArrayList<>(book.getReviews()));
            }

            book.getReviews().add(review);
            book.calculateAverageRating();
            logger.info("Updated average rating for book ID {}: {}", bookId, book.getAverageRating());
        } else {
            logger.error("Book with ID {} not found.", bookId);
        }
    }

    public List<Review> getReviewsByBookId(String bookId, HttpServletRequest request) {
        String reviewsUrl = apiGatewayUrl + "/reviews/book/" + bookId;
        try {
            ResponseEntity<Review[]> response = restTemplate.getForEntity(reviewsUrl, Review[].class);
            Review[] reviews = response.getBody();
            if (reviews != null) {
                UserResponse currentUser = (UserResponse) request.getAttribute("user");
                boolean isLoggedIn = currentUser != null;
                int anonymousCounter = 1;

                for (Review review : reviews) {
                    if (isLoggedIn) {
                        String userUrl = apiGatewayUrl + "/users/id/" + review.getUserId();
                        try {
                            ResponseEntity<UserDTO> userResponse = restTemplate.getForEntity(userUrl, UserDTO.class);
                            UserDTO user = userResponse.getBody();
                            if (user != null) {
                                review.setUsername(user.getUsername());
                            }
                        } catch (HttpClientErrorException e) {
                            // Manejar error de usuario no encontrado
                            review.setUsername("Unknown User");
                        }
                    } else {
                        review.setUsername("Usuario " + anonymousCounter++);
                    }
                }
                return new ArrayList<>(Arrays.asList(reviews)); // Asegurarse de que la lista sea mutable
            } else {
                return new ArrayList<>();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ArrayList<>(); // No reviews found, set empty list
            } else {
                throw e; // Re-throw other errors
            }
        }
    }

    public boolean hasUserPurchasedBook(String userId, String bookId) {
        String ordersUrl = apiGatewayUrl + "/orders/id/" + userId;
        try {
            ResponseEntity<Order[]> response = restTemplate.getForEntity(ordersUrl, Order[].class);
            Order[] orders = response.getBody();
            if (orders != null) {
                for (Order order : orders) {
                    for (CartItemDTO cartItem : order.getCartItems()) {
                        if (cartItem.getBookId().equals(bookId)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false; // No orders found, user has not purchased the book
            } else {
                throw e; // Re-throw other errors
            }
        }
    }


}
