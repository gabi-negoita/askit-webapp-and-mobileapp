package com.project.askit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class Crypto {

    public static final Key KEY = new SecretKeySpec("ASKITYOURSELFKEY".getBytes(), "AES");
    public static final String AES_ALGORITHM = "AES";
    private static final Logger logger = LoggerFactory.getLogger(Crypto.class);

    public static String getShaFromInputStream(InputStream inputStream) {
        try {
            // Create message digest instance
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // Update message digest
            messageDigest.update(inputStream.readAllBytes());

            // Complete hash computation
            byte[] digest = messageDigest.digest();

            // Return hash as string
            return String.format("%064x", new BigInteger(1, digest));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String encrypt(String text,
                                 Key key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedText = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encryptedText,
                                 Key key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedText = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedText = cipher.doFinal(decodedText);

            return new String(decryptedText);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}