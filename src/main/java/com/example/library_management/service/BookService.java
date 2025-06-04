package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepo.findById(id);
    }

    public Book addBook(Book book) {
        book.setAvailability(true);
        return bookRepo.save(book);
    }

    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepo.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepo.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchBooksByCategory(String category) {
        return bookRepo.findByCategoryContainingIgnoreCase(category);
    }
}
