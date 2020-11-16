package com.tomi.payments.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class CryptoService {
    private final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    private static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";

    public String generateKey(int keyLen) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < keyLen; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    public <T> String AESEncrypt(String key, T object) {
        try {
            IvParameterSpec ivspec = buildIvSpec();
            SecretKeySpec secretKey = buildSecretKey(key);

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            final ObjectMapper objectMapper = new ObjectMapper();
            final String rawText = objectMapper.writeValueAsString(object);

            return Base64.getEncoder().encodeToString(cipher.doFinal(rawText.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.error("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public <T> T AESDecrypt(String key, String cipherText, Class<T> objectClass) {
        try {
            IvParameterSpec ivspec = buildIvSpec();
            SecretKeySpec secretKey = buildSecretKey(key);

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            String rawText = new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));

            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(rawText, objectClass);
        } catch (Exception e) {
            logger.error("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private IvParameterSpec buildIvSpec() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        return new IvParameterSpec(iv);
    }

    private SecretKeySpec buildSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), key.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

}
