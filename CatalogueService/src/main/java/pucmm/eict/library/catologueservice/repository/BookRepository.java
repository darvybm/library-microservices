package pucmm.eict.library.catologueservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pucmm.eict.library.catologueservice.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenres(String genre);

    List<Book> findByTitleRegexIgnoreCase(String title);

    List<Book> findByAuthorRegexIgnoreCase(String author);

    List<Book> findByGenresRegexIgnoreCase(String genre);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByGenresContainingIgnoreCase(String genre);
}
