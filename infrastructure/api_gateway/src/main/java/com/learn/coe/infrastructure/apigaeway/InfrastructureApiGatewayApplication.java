package com.learn.coe.infrastructure.apigaeway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author coffee
 * @since 2021-05-29 12:15
 */
@SpringBootApplication
@EnableDiscoveryClient
public class InfrastructureApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApiGatewayApplication.class,args);
    }
}
