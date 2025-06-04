package com.example.library_management.repository;

import com.example.library_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries if needed, for example by name or membershipType
}
