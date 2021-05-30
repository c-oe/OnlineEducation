package com.learn.coe.service.edu.controller.api;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.base.dto.CourseDto;
import com.learn.coe.service.edu.entity.Course;
import com.learn.coe.service.edu.entity.vo.ChapterVo;
import com.learn.coe.service.edu.entity.vo.WebCourseQueryVo;
import com.learn.coe.service.edu.entity.vo.WebCourseVo;
import com.learn.coe.service.edu.service.ChapterService;
import com.learn.coe.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author coffee
 * @since 2021-05-21 15:59
 */
//@CrossOrigin
@Api("课程")
@Slf4j
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public R pageList(@ApiParam(value = "查询对象",required = true) WebCourseQueryVo webCourseQueryVo){
        List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
        return R.ok().data("courseList",courseList);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("get/{courseId}")
    public R getById(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId){
        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询当前课程的嵌套章节和课时信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        return R.ok().data("course",webCourseVo).data("chapterVoList",chapterVoList);
    }


    @ApiOperation("根据ID查询课程")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId){

        return courseService.getCourseDtoById(courseId);
    }

    @ApiOperation("根据课程ID更改销量")
    @GetMapping("inner/update-buy-count/{id}")
    public R updateBuyCountById(@ApiParam(value = "课程ID",required = true)@PathVariable String id){
        courseService.updateBuyCountById(id);
        return R.ok();
    }
}
