package com.learn.coe.service.trade.feign.fallback;

import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author coffee
 * @since 2021-05-27 16:56
 */
@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.info("熔断保护");
        return null;
    }
}
