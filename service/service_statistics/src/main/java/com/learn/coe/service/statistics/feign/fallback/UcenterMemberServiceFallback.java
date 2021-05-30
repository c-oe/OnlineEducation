package com.learn.coe.service.statistics.feign.fallback;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author coffee
 * @since 2021-05-29 14:21
 */

@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {
    @Override
    public R countRegisterNum(String day) {
        log.error("熔断保护");
        return R.ok().data("reisterNum",0);
    }
}
