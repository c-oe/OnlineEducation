package com.learn.coe.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author coffee
 * @since 2021-05-24 16:01
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.learn.coe")
public class ServiceUCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUCenterApplication.class);
    }
}
