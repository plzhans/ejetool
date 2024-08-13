package com.ejetool.jwt.generator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParserBuilder;

public interface JwtValidator  {

    String getKid();

    default Jws<Claims> parseClaims(String token) {
        return parserBuilder()
            .build()
            .parseClaimsJws(token);
    }

    default Claims parseClaimsThenBody(String token) {
        return parserBuilder()
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    default Claims verify(String token) {
        return parseClaimsThenBody(token);
    }

    JwtParserBuilder parserBuilder();
}
