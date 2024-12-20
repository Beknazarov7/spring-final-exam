package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict access to ADMIN endpoints
                .requestMatchers("/users/**").hasAnyRole("ADMIN", "USER") // Allow both ADMIN and USER roles
                .anyRequest().authenticated()
                .and()
                .httpBasic(); // Enable basic authentication
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Example users
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN") // ADMIN role
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER") // USER role
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
