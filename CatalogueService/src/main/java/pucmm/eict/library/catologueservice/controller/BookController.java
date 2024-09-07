package pucmm.eict.library.catologueservice.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.catologueservice.model.Book;
import pucmm.eict.library.catologueservice.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/catalogue")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable ObjectId id) {
        return bookService.getBookBtId(id);
    }

    @GetMapping("/search/title")
    public List<Book> searchByTitle(@RequestParam String title) {
        return bookService.searchByTitle(title);
    }

    @GetMapping("/search/author")
    public List<Book> searchByAuthor(@RequestParam String author) {
        return bookService.searchByAuthor(author);
    }

    @GetMapping("/search/genre")
    public List<Book> searchByGenre(@RequestParam String genre) {
        return bookService.searchByGenre(genre);
    }
}
