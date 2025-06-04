package com.example.library_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status; // e.g. "ISSUED", "RETURNED", "OVERDUE"

    public void setIssueDate(LocalDate now) {
        this.issueDate = now;
    }

    // Constructors, getters, setters
}
