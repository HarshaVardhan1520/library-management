package com.example.library_management.repository;

import com.example.library_management.model.Transaction;
import com.example.library_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    @Query("SELECT t FROM Transaction t WHERE t.status = 'OVERDUE'")
    List<Transaction> findOverdueTransactions();

    // You can add more custom queries if needed
}
