package com.learn.coe.service.edu.feign.fallback;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.feign.OSSFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author coffee
 * @Date 2021-05-17 14:56
 */
@Service
@Slf4j
public class OSSFileServiceFallBack implements OSSFileService {

    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
