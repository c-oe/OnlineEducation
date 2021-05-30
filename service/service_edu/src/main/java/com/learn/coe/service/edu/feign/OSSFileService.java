package com.learn.coe.service.edu.feign;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.feign.fallback.OSSFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author coffee
 * @Date 2021-05-16 23:10
 */
@FeignClient(value = "service-oss",fallback = OSSFileServiceFallBack.class)
public interface OSSFileService {

    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
