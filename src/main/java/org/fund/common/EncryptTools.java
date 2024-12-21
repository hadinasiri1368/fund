package org.fund.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class EncryptTools {
    public static final String SECRET = "R@yan_H@mAfza";

    // The salt (probably) can be stored along with the encrypted data
    private static final byte[] SALT = "45OOkJkl))".getBytes();

    // Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
    private static final int ITERATION_COUNT = 40000;
    // Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
    private static final int KEY_LENGTH = 128;

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
//            digest.update(s.getBytes());
            return byteArrayToHexString(digest.digest(s.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toSHA1(byte[] convertMe) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(md.digest(convertMe));
    }

    public static String toSHA256(String convertMe) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(md.digest(convertMe.getBytes()));
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String encodePassword(String password) {
        return toSHA1(md5(password).getBytes());
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2)
            throw new IllegalArgumentException("bad command found!\n" +
                    "Use \"java -cp commons-1.0.jar com.rh.ot.web.util.EncryptTools {password} {secret}\"");

        String originalPassword = args[0];
        String secret = args[1];
        System.out.println("Original password: " + originalPassword);
        System.out.println("secret: " + secret);
        String encryptedPassword = encrypt(originalPassword, secret);
        System.out.println("Encrypted password: " + encryptedPassword);
        String decryptedPassword = decrypt(encryptedPassword, secret);
        System.out.println("Decrypted password: " + decryptedPassword);
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) {
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = null;
        try {
            keyTmp = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String encrypt(String property, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = createSecretKey(key.toCharArray(),
                SALT, ITERATION_COUNT, KEY_LENGTH);
        pbeCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string, String key) {
        try {
            String iv = string.split(":")[0];

            String property = string.split(":")[1];
            Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = createSecretKey(key.toCharArray(),
                    SALT, ITERATION_COUNT, KEY_LENGTH);
            pbeCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(base64Decode(iv)));
            return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("error while decryption");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}
