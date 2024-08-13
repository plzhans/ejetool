package com.ejetool.jwt.generator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;

import com.ejetool.common.crypto.rsa.RSAKeyGenerator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class JwtKeyStoreValidator {

    public static final String DEFAULT_KID = "-";

    @Getter
    protected final Map<String,Key> store;

    public JwtKeyStoreValidator(){
        this.store = new HashMap<>();
    }

    public String addSecretKey(String secret){
        this.addSecretKey(secret, DEFAULT_KID);
        return DEFAULT_KID;
    }

    public void addSecretKey(String secret, String kid){
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        if(keyBytes.length < 32){
            keyBytes = Arrays.copyOf(keyBytes, 32);
        }
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.addKey(secretKey, kid);
    }


    public void addPublicKeyPath(String path){
        this.addPublicKeyPath(path, JwtKeyStoreValidator.DEFAULT_KID);
    }

    public void addPublicKeyPath(String publicKeyPath, String kid){
        Path path = Paths.get(publicKeyPath);
        PublicKey privateKey = RSAKeyGenerator.loadPublicKey(path);
        this.addKey(privateKey, kid);
    }

    public void addPrivateKeyPath(Path path){
        this.addPrivateKeyPath(path, JwtKeyStoreValidator.DEFAULT_KID);
    }

    public void addPrivateKeyPath(Path path, String kid){
        PrivateKey privateKey = RSAKeyGenerator.loadPrivateKey(path);
        this.addKey(privateKey, kid);
    }

    public String addKey(SecretKey key){
        this.addKey(key, DEFAULT_KID);
        return DEFAULT_KID;
    }

    public String addKey(Key key){
        this.addKey(key, DEFAULT_KID);
        return DEFAULT_KID;
    }
    
    public void addKey(Key key, String kid){
        if(this.store.containsKey(kid)){
            throw new IllegalArgumentException("Key with id " + kid + " already exists.");
        }
        this.store.put(kid, key);
    }

    public void addKey(String kid, Key key){
        this.store.put(kid, key);
    }

    public Claims verify(String token){
        return this.verifyJws(token, null)
            .getBody();
    }

    public Claims verify(String token, Consumer<JwtParserBuilder> builder){
        return this.verifyJws(token, builder)
            .getBody();
    }

    public Jws<Claims> verifyJws(String token){
        return verifyJws(token, null);
    }

    public Jws<Claims> verifyJws(String token, Consumer<JwtParserBuilder> builder){
        JwtParserBuilder parserBuilder = Jwts.parserBuilder()
            .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                @SuppressWarnings("rawtypes")
                @Override
                public Key resolveSigningKey(JwsHeader header, Claims claims) {
                    String kid = header.getKeyId();
                    if (kid == null || !store.containsKey(kid)) {
                        throw new MalformedJwtException("Token is missing kid header.");
                    }
                    return store.get(kid);
                }
            });
        if(builder != null){
            builder.accept(parserBuilder);
        }
        JwtParser parse = parserBuilder.build();
        return parse.parseClaimsJws(token);
    }
}
