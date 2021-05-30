package com.learn.coe.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author coffee
 * @since 2021-05-19 21:56
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.learn.coe"})
@EnableDiscoveryClient
public class serviceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(serviceVodApplication.class,args);
    }
}
