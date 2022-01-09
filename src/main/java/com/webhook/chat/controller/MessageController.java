package com.webhook.chat.controller;

import com.webhook.chat.model.Message;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    public static List<Message> messages = new ArrayList<>();

    @PostMapping
    public Message sendMessage(@RequestBody @Validated Message message) {
        message.setCreatedAt(LocalDateTime.now());
        messages.add(message);
        return message;
    }

    @GetMapping
    public List<Message> readMessages() {
        return messages;
    }

    @Scheduled(fixedDelay = 100)
    public void emptyMessages() {
        messages.removeIf(value -> ChronoUnit.MINUTES.between(value.getCreatedAt(), LocalDateTime.now()) >= 5);
    }

}
