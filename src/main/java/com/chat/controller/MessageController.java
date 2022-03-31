package com.chat.controller;

import com.chat.model.Message;
import com.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public void saveMessage(@RequestBody @Validated Message message) {
        messageService.saveMessage(message);
    }

    @GetMapping
    public List<Message> getMessages(@RequestHeader String key) {
        return messageService.getMessages(key);
    }

}
