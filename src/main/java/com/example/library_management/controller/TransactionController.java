package com.example.library_management.controller;

import com.example.library_management.model.Book;
import com.example.library_management.model.Transaction;
import com.example.library_management.model.User;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.TransactionRepository;
import com.example.library_management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public TransactionController(TransactionRepository transactionRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Lend a book (create transaction)
    @PostMapping("/lend")
    public ResponseEntity<?> lendBook(@RequestParam Long bookId, @RequestParam Long userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (book == null || user == null) {
            return ResponseEntity.badRequest().body("Invalid book or user ID");
        }

        if (!book.isAvailability()) {
            return ResponseEntity.badRequest().body("Book is not available");
        }

        // Mark book as not available
        book.setAvailability(false);
        bookRepository.save(book);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setIssueDate(LocalDate.now());
        transaction.setStatus("ISSUED");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(transaction);
    }

    // Return a book
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction == null || !"ISSUED".equals(transaction.getStatus())) {
            return ResponseEntity.badRequest().body("Invalid transaction or book already returned");
        }

        // Update transaction
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus("RETURNED");
        transactionRepository.save(transaction);

        // Mark book as available
        Book book = transaction.getBook();
        book.setAvailability(true);
        bookRepository.save(book);

        return ResponseEntity.ok(transaction);
    }

    // Get borrowing history for a user
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserBorrowingHistory(@PathVariable Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    // Get all overdue books
    @GetMapping("/overdue")
    public ResponseEntity<List<Transaction>> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        // Assuming overdue = issued and not returned after 14 days
        List<Transaction> overdueTransactions = transactionRepository.findByStatusAndIssueDateBefore("ISSUED", today.minusDays(14));
        return ResponseEntity.ok(overdueTransactions);
    }
}
