package com.example.ChatApp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ChatApp.entity.Message;
import com.example.ChatApp.entity.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);
}