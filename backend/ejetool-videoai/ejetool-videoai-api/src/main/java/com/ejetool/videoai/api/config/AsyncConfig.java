package com.ejetool.videoai.api.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ejetool.common.handler.GlobalAsyncExceptionHandler;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer{

    private final GlobalAsyncExceptionHandler globalAsyncExceptionHandler;

    @Bean
    public GlobalAsyncExceptionHandler globalAsyncExceptionHandler() {
        return new GlobalAsyncExceptionHandler();
    }

    @SuppressWarnings("squid:S125")
    @Override
    public Executor getAsyncExecutor() {
        // CPU 코어수 * CPU 사용률 * (1 + I/O입력시간대기효율)
        int coreCount = Runtime.getRuntime().availableProcessors();
        // float targetCpuUtilization = 0.3f;
        // float blockingCoefficient = 0.1f;
        // int corePoolSize = (int) (coreCount * targetCpuUtilization * (1 + blockingCoefficient));
        int corePoolSize = coreCount - 1;
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setThreadNamePrefix("async-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return globalAsyncExceptionHandler;
    }
}
