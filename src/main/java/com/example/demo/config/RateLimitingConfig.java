package com.example.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitingConfig {

    @Bean
    public Bandwidth freeUserLimit() {
        return Bandwidth.classic(100, Refill.greedy(100, Duration.ofHours(1))); // 100 requests/hour for Free users
    }

    @Bean
    public Bandwidth premiumUserLimit() {
        return Bandwidth.classic(500, Refill.greedy(500, Duration.ofHours(1))); // 500 requests/hour for Premium users
    }
}
