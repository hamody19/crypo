package com.example.myapplication.encryptionalgorithm;

import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

// AES - Advance Encryption Standard
public class AESCipher {
    private static SecretKeySpec secretKeySpec;
    private static byte[] key;

    // set our key
    private static void setKey(String userKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        key = userKey.getBytes(StandardCharsets.UTF_8);
        /* Checksum: error detection method */
        /*Hash function: it's a function to produce checksum*/
        /*Hash Value: it's a numeric value of fixed length that uniquely identifies data*/
        /*
        Message Digest: it's a fix size numeric representation of the contents of the message
        computed by hash function
        */
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    public static String msgEncrypt(String msg, String userKey){
        try {
            setKey(userKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception ignored){ }
        return null;
    }

    public static String msgDecrypt(String msg, String userKey){
        try {
            setKey(userKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(msg)));
        }catch (Exception ignored){ }
        return null;
    }

}
