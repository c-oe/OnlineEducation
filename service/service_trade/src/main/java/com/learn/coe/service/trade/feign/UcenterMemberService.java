package com.learn.coe.service.trade.feign;

import com.learn.coe.service.base.dto.MemberDto;
import com.learn.coe.service.trade.feign.fallback.EduCourseServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author coffee
 * @since 2021-05-27 16:44
 */
@FeignClient(value = "service-ucenter",fallback = EduCourseServiceFallback.class)
public interface UcenterMemberService {


    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    MemberDto getMemberDtoByMemberId(@PathVariable("memberId") String memberId);
}
