package org.java.lessons.springilmiofotoalbum.controller;

import org.java.lessons.springilmiofotoalbum.model.Message;
import org.java.lessons.springilmiofotoalbum.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<String> saveMessage(@RequestBody Message message) {
        messageRepository.save(message);
        return ResponseEntity.ok("Message sent successfully");
    }
}
