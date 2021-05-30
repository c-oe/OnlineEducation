package com.learn.coe.service.cms.feign.fallback;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.cms.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author coffee
 * @since 2021-05-23 11:49
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {

    public R removeFile(String url){
        log.info("熔断保护");
        return R.error().message("调用超时");
    }

}
