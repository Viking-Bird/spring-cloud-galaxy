package com.galaxy.eureka;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;


/**
 * Created by wangpeng on 21/03/2018.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ComputeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputeServiceApplication.class,args);
    }

}