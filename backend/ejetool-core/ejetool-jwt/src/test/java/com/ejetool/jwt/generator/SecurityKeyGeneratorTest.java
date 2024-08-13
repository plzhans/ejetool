package com.ejetool.jwt.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class SecurityKeyGeneratorTest {
    @Test
    void createSecretKeyAsBase64_not_empty() {
        String keys = HMACKeyGenerator.generateAsBase64();
        Assertions.assertFalse(keys.isEmpty());
    }
}
