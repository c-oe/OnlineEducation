package com.learn.coe.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.Teacher;
import com.learn.coe.service.edu.entity.vo.TeacherQueryVo;
import com.learn.coe.service.edu.feign.OSSFileService;
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
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
@Api("讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class TeacherController {


    @Autowired
    private TeacherService teacherService;

    @Autowired
    public OSSFileService ossFileService;

    @ApiOperation("所有讲师")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation("根据ID删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam("讲师ID") @PathVariable("id") String id){
        //删除讲师头像
        teacherService.removeById(id);

        //删除讲师
        boolean result = teacherService.removeById(id);
        if (result){
            return R.ok().message("删除成功");
        }else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码",required = true) @PathVariable("page") Long page,
                      @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQueryVo){
        Page<Teacher> teacherPage = new Page<>(page, limit);
        IPage<Teacher> iPage = teacherService.selectPage(teacherPage,teacherQueryVo);
        List<Teacher> records = iPage.getRecords();
        long total = iPage.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if (save){
            return R.ok().message("保存成功");
        }else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("更新讲师")
    @PutMapping ("update")
    public R updateById(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        boolean update = teacherService.updateById(teacher);
        if (update){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id获取讲师信息")
    @PutMapping ("get/{id}")
    public R getById(@ApiParam("讲师对象") @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if (teacher != null){
            return R.ok().data("item",teacher);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("batch-remove")
    public R batchRemove(@ApiParam(value = "讲师ID列表",required = true) List<String> idList){
        boolean remove = teacherService.removeByIds(idList);
        if (remove){
            return R.ok().message("删除成功");
        }else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据关键字查询讲师名列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(@ApiParam(value = "关键字",required = true)
                                     @PathVariable String key){
        List<Map<String,Object>> nameList = teacherService.selectNameList(key);
        return R.ok().data("items",nameList);
    }

    @ApiOperation("测试服务调用")
    @GetMapping("test")
    public R test(){
        ossFileService.test();
        return R.ok();
    }

    @ApiOperation("测试并发")
    @GetMapping("test_concurrent")
    public R testConcurrent(){
        log.info("test_concurrent");
        return R.ok();
    }
}

