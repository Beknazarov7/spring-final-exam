package com.example.demo.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RateLimitingFilter implements Filter {

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final Bandwidth freeUserLimit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofHours(1)));
    private final Bandwidth premiumUserLimit = Bandwidth.classic(500, Refill.greedy(500, Duration.ofHours(1)));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String userRole = httpRequest.getHeader("X-User-Role");

        if (userRole == null) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().write("Missing X-User-Role header");
            return;
        }

        Bucket bucket = getBucket(userRole);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.getWriter().write("Too Many Requests");
        }
    }

    private Bucket getBucket(String userRole) {
        return buckets.computeIfAbsent(userRole, role -> {
            if ("Free".equalsIgnoreCase(role)) {
                return Bucket4j.builder().addLimit(freeUserLimit).build();
            } else if ("Premium".equalsIgnoreCase(role)) {
                return Bucket4j.builder().addLimit(premiumUserLimit).build();
            }
            return Bucket4j.builder().addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofHours(1)))).build();
        });
    }
}
