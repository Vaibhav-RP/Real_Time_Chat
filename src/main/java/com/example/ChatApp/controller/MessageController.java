package com.example.ChatApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChatApp.entity.Message;
import com.example.ChatApp.entity.User;
import com.example.ChatApp.repository.MessageRepository;
import com.example.ChatApp.repository.UserRepository;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate,UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        // Save the message in the database
        Message savedMessage = messageRepository.save(message);
        
        // Broadcast the message to the recipient using Websocket
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getUsername(),
                "/queue/messages",
                savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/{sender}/{receiver}")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String sender,
            @PathVariable String receiver) {
        Optional<User> senderUser = userRepository.findByUsername(sender);
        Optional<User> receiverUser = userRepository.findByUsername(receiver);

        if (senderUser.isPresent() && receiverUser.isPresent()) {
            List<Message> messages = messageRepository.findBySenderAndReceiver(senderUser.get(), receiverUser.get());
            return ResponseEntity.ok(messages);
        }

        return ResponseEntity.notFound().build();
    }
}

