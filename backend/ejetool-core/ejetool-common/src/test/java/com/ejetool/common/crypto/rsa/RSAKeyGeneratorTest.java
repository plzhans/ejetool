package com.ejetool.common.crypto.rsa;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RSAKeyGeneratorTest {
    @Disabled()
    @Test
    void generate_save_file(){
        var keyPair = RSAKeyGenerator.generate();
        RSAKeyGenerator.saveAs(keyPair, "../../keys", "serivce_auth");
    }
}
