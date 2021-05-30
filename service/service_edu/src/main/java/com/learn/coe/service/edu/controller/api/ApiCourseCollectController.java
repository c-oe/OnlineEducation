package com.learn.coe.service.edu.controller.api;


import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.util.JwtInfo;
import com.learn.coe.common.base.util.JwtUtils;
import com.learn.coe.service.edu.entity.vo.CourseCollectVo;
import com.learn.coe.service.edu.service.CourseCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
//@CrossOrigin
@Api("收藏课程")
@Slf4j
@RestController
@RequestMapping("/api/edu/course-collect")
public class ApiCourseCollectController {

    @Autowired
    private CourseCollectService courseCollectService;

    @ApiOperation("判断课程是否收藏")
    @GetMapping("auth/is-collect/{courseId}")
    public R isCollect(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId,
                       HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean isCollect = courseCollectService.isCollect(courseId, jwtInfo.getId());
        return R.ok().data("isCollect",isCollect);
    }

    @ApiOperation("收藏课程")
    @PostMapping("auth/save/{courseId}")
    public R save(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId,
                  HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        courseCollectService.saveCourseCollect(courseId,jwtInfo.getId());
        return R.ok().message("收藏成功");
    }

    @ApiOperation("收藏课程列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<CourseCollectVo> list = courseCollectService.selectListByMemberId(jwtInfo.getId());
        return R.ok().data("items",list);
    }

    @ApiOperation("取消收藏")
    @DeleteMapping("auth/remove/{courseId}")
    public R remove(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId,
                    HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean remove = courseCollectService.removeCourseCollect(courseId, jwtInfo.getId());
        if (remove){
            return R.ok().message("取消收藏成功");
        }else {
            return R.error().message("取消收藏失败");
        }
    }
}

