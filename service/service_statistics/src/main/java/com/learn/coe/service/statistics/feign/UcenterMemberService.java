package com.learn.coe.service.statistics.feign;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.statistics.feign.fallback.UcenterMemberServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author coffee
 * @since 2021-05-29 14:20
 */
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallback.class)
public interface UcenterMemberService {

    @GetMapping("/admin/ucenter/member/count-register-num/{day}")
    R countRegisterNum(@PathVariable("day") String day);
}
