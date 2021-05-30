package com.learn.coe.service.edu.controller.api;

import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.Teacher;
import com.learn.coe.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author coffee
 * @since 2021-05-21 15:20
 */
//@CrossOrigin
@Api("讲师")
@Slf4j
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("所有讲师")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("获取成功");
    }

    @ApiOperation("获取讲师")
    @GetMapping("get/{id}")
    public R get(@ApiParam(value = "讲师ID",required = true)@PathVariable String id){
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map);
    }
}
