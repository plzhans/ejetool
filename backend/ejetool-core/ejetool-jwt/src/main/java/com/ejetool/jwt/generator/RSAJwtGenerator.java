package com.ejetool.jwt.generator;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.nio.file.Path;

import com.ejetool.common.crypto.rsa.RSAKeyGenerator;

@Slf4j
public class RSAJwtGenerator extends RSAJwtValidator implements JwtGenerator {

    @Getter
    private final KeyPair keyPair;

    public static RSAJwtGenerator create(Path keyPath){
        return create(DEFAULT_KID, keyPath);
    }

    public static RSAJwtGenerator create(String kid, Path keyPath){
        KeyPair keyPair = RSAKeyGenerator.loadKeyPair(keyPath);
        return new RSAJwtGenerator(kid, keyPair);
    }

    public RSAJwtGenerator(String kid, KeyPair keyPair){
        super(kid, keyPair.getPublic());
        this.keyPair = keyPair;
    }

    public RSAJwtGenerator(String kid, PublicKey publicKey, PrivateKey privaeKey){
        super(kid, publicKey);
        this.keyPair = new KeyPair(publicKey, privaeKey);
    }

    @Override
    public JwtBuilder builder(){
        JwtBuilder builder = Jwts.builder()
            .signWith(this.keyPair.getPrivate(), SignatureAlgorithm.RS256);
        return builder;
    }
}
