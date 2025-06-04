package com.example.library_management.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String author;
    private String category;
    private boolean availability;

    public void setAvailability(boolean b) {
        this.availability = b;
    }
}
