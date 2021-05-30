package com.learn.coe.service.edu.feign.fallback;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.feign.VODMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author coffee
 * @since 2021-05-20 13:05
 */
@Service
@Slf4j
public class VODFileServiceFallBack implements VODMediaService {


    @Override
    public R removeVideo(String vodId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> vodIdList) {
        log.info("熔断保护");
        return R.error();
    }
}
