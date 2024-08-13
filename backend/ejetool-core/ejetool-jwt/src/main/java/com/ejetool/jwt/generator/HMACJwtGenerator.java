package com.ejetool.jwt.generator;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Arrays;
import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HMACJwtGenerator implements JwtGenerator {

    public static final String DEFAULT_KID = "secret";

    @Getter
    private final String kid;

    @Getter
    private final SecretKey secretKey;

    public static HMACJwtGenerator create(String secret){
        return create(DEFAULT_KID, secret);
    }

    public static HMACJwtGenerator create(String kid, String secret){
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        if(keyBytes.length < 32){
            keyBytes = Arrays.copyOf(keyBytes, 32);
        }
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return new HMACJwtGenerator(kid, secretKey);
    }

    /**
     * 
     * @param issuer
     * @param secret base64 string
     */
    public HMACJwtGenerator(String kid, SecretKey secretKey){
        this.kid = kid;
        this.secretKey = secretKey;
    }

    @Override
    public JwtBuilder builder(){
        return Jwts.builder()
            .signWith(this.secretKey)
            .setHeaderParam("kid", this.kid);
    }

    @Override
    public JwtParserBuilder parserBuilder() {
        return Jwts.parserBuilder()
            .setSigningKey(this.secretKey);
           
    }
}
