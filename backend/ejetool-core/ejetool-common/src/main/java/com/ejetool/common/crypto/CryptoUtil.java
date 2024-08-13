package com.ejetool.common.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.ejetool.common.exception.CryptoException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CryptoUtil {

    public static String toSHA256String(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }
}
