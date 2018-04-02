package com.galaxy.consul.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class SpringCloudConsulSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsulSchoolApplication.class, args);
    }
}