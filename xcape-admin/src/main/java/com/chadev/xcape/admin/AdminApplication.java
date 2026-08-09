package com.chadev.xcape.admin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
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
        // datasource 초기화 전에 타임존이 고정되어야 하므로 반드시 첫 줄에서 설정한다.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(AdminApplication.class, args);
    }
}
