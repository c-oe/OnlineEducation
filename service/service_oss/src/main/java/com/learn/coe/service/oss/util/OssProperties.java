package com.learn.coe.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author coffee
 * @Date 2021-05-15 16:40
 */
@ConfigurationProperties(prefix = "aliyun.oss")
@Component
@Data
public class OssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
}
