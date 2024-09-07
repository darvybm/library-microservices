package pucmm.eict.library.catologueservice.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pucmm.eict.library.catologueservice.model.Book;
import pucmm.eict.library.catologueservice.repository.BookRepository;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchByGenre(String genre) {
        return bookRepository.findByGenresContainingIgnoreCase(genre);
    }

    public Book getBookBtId(ObjectId id) {
        return bookRepository.findById(id).orElse(null);
    }
}
