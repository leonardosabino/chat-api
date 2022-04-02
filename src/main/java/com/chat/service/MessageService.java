package com.chat.service;

import com.chat.model.Message;
import com.chat.model.MessageByte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private CryptoService cryptoService;

    public static List<MessageByte> messagesBytes = new ArrayList<>();

    public void saveMessage(Message message) {

        byte[] messageBytes = cryptoService.encrypt(message.getMessage().getBytes(StandardCharsets.UTF_8));
        byte[] nickNameBytes = cryptoService.encrypt(message.getNickName().getBytes(StandardCharsets.UTF_8));

        var messageByteBuilder = MessageByte.builder()
                .message(messageBytes)
                .nickName(nickNameBytes)
                .createdAt(LocalDateTime.now())
                .build();

        messagesBytes.add(messageByteBuilder);
    }

    public List<Message> getMessages(String key) {
        return messagesBytes.stream().map(messageByte -> Message.builder()
                .message(new String(cryptoService.decrypt(key.getBytes(StandardCharsets.UTF_8), messageByte.getMessage())))
                .nickName(new String(cryptoService.decrypt(key.getBytes(StandardCharsets.UTF_8), messageByte.getNickName())))
                .build()).collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 100)
    private void emptyMessages() {
        messagesBytes.removeIf(value -> ChronoUnit.MINUTES.between(value.getCreatedAt(), LocalDateTime.now()) >= 5);
    }

}
