package com.example.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaRepositories
@EnableJpaAuditing
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class PlaygroundApplication {


    public static void main(String[] args) {
        SpringApplication.run(PlaygroundApplication.class, args);
    }


}
