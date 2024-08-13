package com.ejetool.jwt.generator;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.HashMap;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.AsymmetricKey;
import java.nio.file.Path;
import javax.crypto.SecretKey;

import com.ejetool.common.crypto.rsa.RSAKeyGenerator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtKeyStoreGenerator{

    @Getter
    private final Map<String,Key> store;

    @Getter
    private final Map<JwtKeyType, String> typeMap;

    public JwtKeyStoreGenerator(){
        this.store = new HashMap<>();
        this.typeMap = new EnumMap<>(JwtKeyType.class);
    }

    public void addSecretKey(String secret){
        this.addSecretKey(secret, JwtKeyStoreValidator.DEFAULT_KID);
    }

    public void addSecretKey(String secret, String kid){
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        if(keyBytes.length < 32){
            keyBytes = Arrays.copyOf(keyBytes, 32);
        }
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.addKey(secretKey, kid, JwtKeyType.SYMMETRIC);
    }

    public void addPublicKeyPath(Path path){
        this.addPublicKeyPath(path, JwtKeyStoreValidator.DEFAULT_KID);
    }

    public void addPublicKeyPath(Path path, String kid){
        PublicKey privateKey = RSAKeyGenerator.loadPublicKey(path);
        this.addKey(privateKey, kid, JwtKeyType.ASYMMETRIC);
    }

    public void addPrivateKeyPath(Path path){
        this.addPrivateKeyPath(path, JwtKeyStoreValidator.DEFAULT_KID);
    }

    public void addPrivateKeyPath(Path path, String kid){
        PrivateKey privateKey = RSAKeyGenerator.loadPrivateKey(path);
        this.addKey(privateKey, kid, JwtKeyType.ASYMMETRIC);
    }

    public void addKey(AsymmetricKey key){
        this.addKey(key, JwtKeyStoreValidator.DEFAULT_KID, JwtKeyType.ASYMMETRIC);
    }

    public void addKey(AsymmetricKey key, String kid){
        this.addKey(key, kid, JwtKeyType.ASYMMETRIC);
    }

    public void addKey(SecretKey key){
        this.addKey(key, JwtKeyStoreValidator.DEFAULT_KID, JwtKeyType.SYMMETRIC);
    }

    public void addKey(SecretKey key, String kid){
        this.addKey(key, kid, JwtKeyType.SYMMETRIC);
    }

    public void addKey(Key key, String kid, JwtKeyType keyType){
        if(this.store.containsKey(kid)){
            throw new IllegalArgumentException("Key with id " + kid + " already exists.");
        }
        this.store.put(kid, key);
        if(!this.typeMap.containsKey(keyType)){
            this.typeMap.put(keyType, kid);
        }
    }

    public JwtBuilder builder(JwtKeyType keyType, String sub){
        return builder(keyType)
            .setSubject(sub);
    }


    public JwtBuilder builder(JwtKeyType keyType){
        String kid = typeMap.get(keyType);
        return builder(kid);
    }

    public JwtBuilder builder(String kid){
        Key key = store.get(kid);
        JwtBuilder builder = Jwts.builder()
            .signWith(key)
            .setHeaderParam("kid", kid);
        return builder;
    }
}
