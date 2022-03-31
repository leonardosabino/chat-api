package com.chat.service;

import com.chat.model.Message;
import com.chat.model.MessageByte;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    public static List<MessageByte> messagesBytes = new ArrayList<>();

    @Value("${chat.key}")
    public String keyProperty;

    public void saveMessage(Message message) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(keyProperty.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] messageBytes = cipher.doFinal(message.getMessage().getBytes(StandardCharsets.UTF_8));
            byte[] nickNameBytes = cipher.doFinal(message.getNickName().getBytes(StandardCharsets.UTF_8));

            var messageByteBuilder = MessageByte.builder()
                    .message(messageBytes)
                    .nickName(nickNameBytes)
                    .createdAt(LocalDateTime.now())
                    .build();

            messagesBytes.add(messageByteBuilder);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Message> getMessages(String key) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return messagesBytes.stream().map(messageByte -> {
                try {
                    return Message.builder()
                            .message(new String(cipher.doFinal(messageByte.getMessage())))
                            .nickName(new String(cipher.doFinal(messageByte.getNickName())))
                            .build();
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).collect(Collectors.toList());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException  e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 100)
    private void emptyMessages() {
        messagesBytes.removeIf(value -> ChronoUnit.MINUTES.between(value.getCreatedAt(), LocalDateTime.now()) >= 5);
    }

}
