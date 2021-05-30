package com.learn.coe.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author coffee
 * @since 2021-05-19 22:04
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.vod")
public class VodProperties {

    private String KeyId;
    private String keySecret;
    private String templateGroupId;
    private String workflowId;
}
