package com.chat.service;

import com.chat.exception.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CryptoService {

    @Value("${chat.key}")
    public String keyProperty;

    private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String CIPHER_ALGORITHM = "AES";

    public byte[] encrypt(byte[] message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            SecretKey secretKey = new SecretKeySpec(keyProperty.getBytes(StandardCharsets.UTF_8), CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(message);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage());
        }
    }

    public byte[] decrypt(byte[] key, byte[] message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            SecretKey secretKey = new SecretKeySpec(key, CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(message);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage());
        }
    }
}
