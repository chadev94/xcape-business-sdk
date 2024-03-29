package com.chadev.xcape.admin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@EnableJpaAuditing
@EnableEncryptableProperties
@EntityScan(basePackages = "com.chadev.xcape.core.domain.entity")
@EnableJpaRepositories(basePackages = {"com.chadev.xcape.core.repository"})
@SpringBootApplication (scanBasePackages = {"com.chadev.xcape.admin", "com.chadev.xcape.core"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // timezone 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
