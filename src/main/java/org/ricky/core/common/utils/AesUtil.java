package org.ricky.core.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className AesUtil
 * @desc
 */
public class AesUtil {

    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String IV = "1234567890123456";

    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(UTF_8));

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(UTF_8));

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, UTF_8);
    }

}
