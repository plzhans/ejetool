package com.ejetool.common.cache.generator;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;

import com.ejetool.common.crypto.CryptoUtil;

public class ParametersAllHashKeyGenerator implements KeyGenerator {

    @Override
    public @NonNull Object generate(@NonNull Object target, @NonNull Method method, @NonNull Object... params) {
        String key = Arrays.deepToString(params);
        return CryptoUtil.toSHA256String(key);
    }

}
