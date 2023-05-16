package com.chadev.xcape.admin.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeHttpRequests(request ->
                        request
                                .dispatcherTypeMatchers(DispatcherType.FORWARD)
                                .permitAll()
                                .anyRequest().authenticated())
                .formLogin(login ->
                        login
                                .defaultSuccessUrl("/", true))
                .logout(withDefaults());

        return http.build();
    }
}
