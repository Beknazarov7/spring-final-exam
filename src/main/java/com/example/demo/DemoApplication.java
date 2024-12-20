package com.example.demo;

import com.example.demo.filter.RateLimitingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import jakarta.servlet.Filter;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<Filter> rateLimitingFilterRegistration(RateLimitingFilter rateLimitingFilter) {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(rateLimitingFilter);
		registrationBean.setOrder(1); // Set filter order
		return registrationBean;
	}
}
