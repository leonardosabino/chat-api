package com.chat.service;

import com.chat.exception.CryptoException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
public class CryptoServiceTest {

    private static final String message = "MOCK MESSAGE";

    @InjectMocks
    private CryptoService cryptoService;

    @Value("${chat.key}")
    public String keyProperty;

    @BeforeEach
    public void before() throws IllegalAccessException {
        FieldUtils.writeField(cryptoService, "keyProperty", keyProperty, true);
    }

    @Test
    @DisplayName("Should encrypt and decrypt a message with success")
    public void test01() {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] messageEncrypted = cryptoService.encrypt(messageBytes);
        byte[] messageDecrypt = cryptoService.decrypt(keyProperty.getBytes(StandardCharsets.UTF_8), messageEncrypted);

        assertEquals(message, new String(messageDecrypt));
    }

    @Test
    @DisplayName("Should throw error when decrypt a message with wrong secret")
    public void test02() {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] messageEncrypted = cryptoService.encrypt(messageBytes);

        assertThrows(CryptoException.class, () -> {
            cryptoService.decrypt("WRONG_SECRET".getBytes(), messageEncrypted);
        });
    }

}
