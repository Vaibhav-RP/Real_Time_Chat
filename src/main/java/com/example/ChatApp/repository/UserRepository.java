
package com.example.ChatApp.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ChatApp.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
