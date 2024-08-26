package com.wxchen.videosite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "customExecutor")
    public Executor asyncExecutor() {
        // 虚拟线程
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
