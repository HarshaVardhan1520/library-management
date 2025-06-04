package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.model.Transaction;
import com.example.library_management.model.User;
import com.example.library_management.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookService bookService;

    public TransactionService(TransactionRepository transactionRepository, BookService bookService) {
        this.transactionRepository = transactionRepository;
        this.bookService = bookService;
    }

    // Issue a book to user
    public Transaction issueBook(Book book, User user) {
        if (!book.isAvailability()) {
            throw new IllegalStateException("Book is not available for lending.");
        }

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setIssueDate(LocalDate.now());
        transaction.setStatus("ISSUED");
        book.setAvailability(false);  // Mark book as not available

        bookService.saveBook(book);   // Update book availability

        return transactionRepository.save(transaction);
    }

    // Return book
    public Transaction returnBook(Long transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found.");
        }
        Transaction transaction = transactionOpt.get();
        if ("RETURNED".equalsIgnoreCase(transaction.getStatus())) {
            throw new IllegalStateException("Book already returned.");
        }

        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus("RETURNED");

        // Mark book available again
        Book book = transaction.getBook();
        book.setAvailability(true);
        bookService.saveBook(book);

        return transactionRepository.save(transaction);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get overdue transactions (status = ISSUED and issueDate + some days < today)
    public List<Transaction> getOverdueTransactions() {
        LocalDate today = LocalDate.now();
        // Assume lending period is 14 days
        LocalDate overdueDate = today.minusDays(14);

        return transactionRepository.findByStatusAndIssueDateBefore("ISSUED", overdueDate);
    }

    // Get borrowing history by user
    public List<Transaction> getBorrowingHistoryByUser(Long userId) {
        return transactionRepository.findByUserUserId(userId);
    }
}

