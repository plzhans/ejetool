package com.ejetool.jwt.generator;

import io.jsonwebtoken.JwtBuilder;

import java.util.function.Consumer;

public interface JwtGenerator extends JwtValidator {

    default String build(String iss, String sub) {
        JwtBuilder jwtBuilder = builder()
            .setIssuer(iss)
            .setSubject(sub);
        return jwtBuilder.compact();
    }

    default String build(String iss, String sub, Consumer<JwtBuilder> builder) {
        JwtBuilder jwtBuilder = builder()
            .setIssuer(iss)
            .setSubject(sub);
        builder.accept(jwtBuilder);
        return jwtBuilder.compact();
    }

    default String build(String sub, Consumer<JwtBuilder> builder) {
        JwtBuilder jwtBuilder = builder()
            .setSubject(sub);
        builder.accept(jwtBuilder);
        return jwtBuilder.compact();
    }

    default JwtBuilder builder(String iss, String sub) {
        JwtBuilder builder = builder()
            .setIssuer(iss)
            .setSubject(sub);
        return builder;
    }

    default JwtBuilder builder(String sub) {
        JwtBuilder builder = builder()
            .setSubject(sub);
        return builder;
    }

    JwtBuilder builder();
}
