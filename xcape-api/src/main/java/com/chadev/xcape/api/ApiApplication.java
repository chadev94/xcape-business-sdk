package com.chadev.xcape.api;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@EnableEncryptableProperties
@EntityScan(basePackages = "com.chadev.xcape.core.domain.entity")
@EnableJpaRepositories(basePackages = {"com.chadev.xcape.core.repository", "com.chadev.xcape.api.repository"})
@SpringBootApplication (scanBasePackages = {"com.chadev.xcape.api", "com.chadev.xcape.core"})
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
