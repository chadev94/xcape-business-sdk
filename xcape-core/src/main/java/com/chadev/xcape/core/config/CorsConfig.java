package com.chadev.xcape.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://xcape-front-app.web.app",
                "http://xcapeportal-env.eba-wvrpqtqj.ap-northeast-1.elasticbeanstalk.com/",
                "http://xcape-admin.ap-northeast-1.elasticbeanstalk.com",
                "https://xcape.kr",
                "https://xcapesw.co.kr",
                "https://x-crime.com",
                "https://xcape.co.kr"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
