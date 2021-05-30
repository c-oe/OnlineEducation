package com.learn.coe.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author coffee
 * @since 2021-05-23 11:04
 */
@SpringBootApplication
@ComponentScan("com.learn.coe")
@EnableFeignClients
public class ServiceCMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCMSApplication.class,args);
    }
}
