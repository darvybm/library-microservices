package pucmm.eict.library.catologueservice.util;

import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pucmm.eict.library.catologueservice.model.Book;
import pucmm.eict.library.catologueservice.service.BookService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BookGenerator implements ApplicationRunner {

    private final BookService bookService;
    private final boolean generateBooksOnStartup;
    private final int generateBooksCount;

    public BookGenerator(BookService bookService,
                         @Value("${library.catalogue.generate-books-on-startup:true}") boolean generateBooksOnStartup,
                         @Value("${library.catalogue.generate-books-count:100}") int generateBooksCount) {
        this.bookService = bookService;
        this.generateBooksOnStartup = generateBooksOnStartup;
        this.generateBooksCount = generateBooksCount;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (generateBooksOnStartup) {
            System.out.println("Iniciando carga inicial de " + generateBooksCount + " libros...");
            generateBooks(generateBooksCount);
            System.out.println("Carga inicial de libros completada.");
        } else {
            System.out.println("La carga inicial de libros está desactivada.");
        }
    }

    public void generateBooks(int numberOfBooks) throws IOException {
        Faker faker = new Faker();
        for (int i = 0; i < numberOfBooks; i++) {
            Book book = new Book();
            book.setId(new ObjectId());
            book.setTitle(faker.book().title());
            book.setDescription(faker.lorem().paragraph());
            book.setAuthor(faker.book().author());
            book.setGenres(generateRandomGenres(faker));
            book.setPrice(faker.number().randomDouble(2, 1, 100));
            book.setCover(getImageUrlFromPicsum());
            book.setCreatedAt(generateRandomDate());
            bookService.addBook(book);
        }
    }

    private List<String> generateRandomGenres(Faker faker) {
        Random random = new Random();
        int numberOfGenres = 1 + random.nextInt(4); // Genera un número aleatorio entre 1 y 4
        return Stream.generate(faker.book()::genre)
                .distinct()
                .limit(numberOfGenres)
                .filter(genre -> !genre.isEmpty())
                .collect(Collectors.toList());
    }

    private LocalDateTime generateRandomDate() {
        Faker faker = new Faker();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastDate = now.minusYears(5);
        long days = ChronoUnit.DAYS.between(pastDate.toLocalDate(), now.toLocalDate());
        long randomDays = faker.random().nextLong(days);
        return pastDate.plusDays(randomDays);
    }

    private String getImageUrlFromPicsum() throws IOException {
        URL url = new URL("https://picsum.photos/600/400");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false); // Disable automatic redirects
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode >= 300 && responseCode < 400) { // Follow redirects
            String location = connection.getHeaderField("Location");
            if (location != null) {
                url = new URL(location);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                connection.getResponseCode();
            }

            System.out.println("LOCATION");
            System.out.println(location);
        }

        return url.toString();
    }
}
