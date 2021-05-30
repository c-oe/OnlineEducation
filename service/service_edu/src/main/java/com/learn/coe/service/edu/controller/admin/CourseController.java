package com.learn.coe.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.form.CourseInfoForm;
import com.learn.coe.service.edu.entity.vo.CoursePublishVo;
import com.learn.coe.service.edu.entity.vo.CourseQueryVo;
import com.learn.coe.service.edu.entity.vo.CourseVo;
import com.learn.coe.service.edu.service.CourseService;
import com.learn.coe.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
//@CrossOrigin
@Api("课程管理")
@Slf4j
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(@ApiParam(value = "课程基本信息",required = true)
                                @RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId).message("保存成功");
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(@ApiParam(value = "课程ID",required = true)
                     @PathVariable String id){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        if (courseInfoForm != null){
            return R.ok().data("item",courseInfoForm);
        }else{
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info")
    public R updateById(@ApiParam(value = "课程ID",required = true)
                     @RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("修改成功");
    }


    @ApiOperation("课程分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码",required = true) @PathVariable("page") Long page,
                      @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Long limit,
                      @ApiParam("课程列表查询对象") CourseQueryVo courseQueryVo){
        IPage<CourseVo> iPage = courseService.selectPage(page,limit,courseQueryVo);
        List<CourseVo> records = iPage.getRecords();
        long total = iPage.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("根据ID删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam("课程ID") @PathVariable String id){

        //删除课程视频，调用vod中删除视频文件的接口
        videoService.removeMediaVideoByCourseId(id);

        //删除课程封面
        courseService.removeCoverById(id);

        //删除课程
        if (courseService.removeCourseById(id)){
            return R.ok().message("删除成功");
        }else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(@ApiParam(value = "课程ID",required = true)@PathVariable String id){

        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);

        if (coursePublishVo != null){
            return R.ok().data("item",coursePublishVo);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(@ApiParam(value = "课程ID",required = true)@PathVariable String id){
        boolean course = courseService.publishCourseById(id);

        if (course){
            return R.ok().message("发布成功");
        }else{
            return R.error().message("数据不存在");
        }
    }
}

