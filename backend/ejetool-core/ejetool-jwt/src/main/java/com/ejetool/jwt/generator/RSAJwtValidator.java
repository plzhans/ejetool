package com.ejetool.jwt.generator;

import java.security.PublicKey;
import java.nio.file.Path;

import com.ejetool.common.crypto.rsa.RSAKeyGenerator;

import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import lombok.Getter;

public class RSAJwtValidator implements JwtValidator {

    public static final String DEFAULT_KID = "rsa";

    @Getter
    private final String kid;

    @Getter
    private final PublicKey publicKey;

    public static RSAJwtValidator create(Path keyPath){
        return create(DEFAULT_KID, keyPath);
    }

    public static RSAJwtValidator create(String kid, Path keyPath){
        PublicKey publicKey = RSAKeyGenerator.loadPublicKey(keyPath);
        return new RSAJwtValidator(kid, publicKey);
    }

    public RSAJwtValidator(String kid, PublicKey publicKey){
        this.kid = kid;
        this.publicKey = publicKey;
    }

    public JwtParserBuilder parserBuilder() {
        return Jwts.parserBuilder()
            .setSigningKey(this.publicKey);
    }
    
}
