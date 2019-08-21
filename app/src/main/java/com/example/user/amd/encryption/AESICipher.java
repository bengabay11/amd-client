package com.example.user.amd.encryption;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.example.user.amd.interfaces.ICipher;

public class AESICipher implements ICipher {
    private String key;
    private byte[] keyBytes;

    private final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private final String aesEncryptionAlgorithm = "AES";

    private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    public AESICipher(String secretKey)
    {
        key = secretKey;
        SecureRandom random = new SecureRandom();
        AESICipher.ivBytes = new byte[16];
        random.nextBytes(AESICipher.ivBytes);
    }

    @Override
    public String encrypt(final String plain) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
    {
        return Base64.encodeToString(this.encryptBytes(plain.getBytes()), Base64.DEFAULT);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decrypt(final String plain) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException
    {
        byte[] encryptedBytes = this.decryptBytes(Base64.decode(plain, 0));
        return new String(encryptedBytes);

    }

    private byte[] encryptBytes(byte[] mes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        String characterEncoding = "UTF-8";
        keyBytes = key.getBytes(characterEncoding);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(keyBytes);
        keyBytes = md.digest();

        SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransformation);

        SecureRandom random = new SecureRandom();
        AESICipher.ivBytes = new byte[16];
        random.nextBytes(AESICipher.ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, newKey, random);
        byte[] destination = new byte[ivBytes.length + mes.length];
        System.arraycopy(ivBytes, 0, destination, 0, ivBytes.length);
        System.arraycopy(mes, 0, destination, ivBytes.length, mes.length);
        return  cipher.doFinal(destination);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] decryptBytes(byte[] bytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        keyBytes = key.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(keyBytes);
        keyBytes = md.digest();

        byte[] ivB = Arrays.copyOfRange(bytes,0,16);
        byte[] codB = Arrays.copyOfRange(bytes,16,bytes.length);


        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivB);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(codB);
    }
}