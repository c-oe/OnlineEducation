package com.learn.coe.service.trade.feign.fallback;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.base.dto.CourseDto;
import com.learn.coe.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author coffee
 * @since 2021-05-27 16:55
 */
@Slf4j
public class EduCourseServiceFallback implements EduCourseService {
    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateBuyCountById(String id) {
        log.info("熔断保护");
        return R.error();
    }
}
