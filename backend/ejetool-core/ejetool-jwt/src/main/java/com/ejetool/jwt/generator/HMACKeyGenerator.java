package com.ejetool.jwt.generator;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;

import java.util.Base64;

@UtilityClass
public class HMACKeyGenerator {

    /**
     * 
     * @return SecretKey - HS256 
     */
    public static SecretKey generate(){
        return generate(SignatureAlgorithm.HS256);
    }

    /**
     * 
     * @return SecretKey - HS256 byte[]
     */
    public static byte[] generateAsBytes(){
        return generateAsBytes(SignatureAlgorithm.HS256);
    }

    /**
     * 
     * @return SecretKey - HS256 base64
     */
    public static String generateAsBase64(){
        return generateAsBase64(SignatureAlgorithm.HS256);
    }

    /**
     * @param HS256, HS384, HS512
     * @return SecretKey
     */
    public static SecretKey generate(SignatureAlgorithm alg){
        return Keys.secretKeyFor(alg);
    }

    /**
     * @param HS256, HS384, HS512
     * @return byte[]
     */
    public static byte[] generateAsBytes(SignatureAlgorithm alg){
        return generate(alg).getEncoded();
    }

    /**
     * @param HS256, HS384, HS512
     * @return base64
     */
    public static String generateAsBase64(SignatureAlgorithm alg){
        byte[] bytes = generateAsBytes(alg);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 
     * @param secret
     * @return
     */
    public static SecretKey createByBase64(String secret){
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        if(keyBytes.length < 32){
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
            keyBytes = paddedKey;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
