package com.learn.coe.service.edu.controller.admin;


import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.Video;
import com.learn.coe.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
//@CrossOrigin
@Api("课时管理")
@Slf4j
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课时")
    @GetMapping("save")
    public R save(@ApiParam(value = "课时对象",required = true)@RequestBody Video video){
        boolean save = videoService.save(video);
        if (save){
            return R.ok().message("保存成功");
        }else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据ID查询课时")
    @PostMapping ("get/{id}")
    public R getById(@ApiParam(value = "课程ID",required = true)@PathVariable String id){
        Video video = videoService.getById(id);
        if (video != null){
            return R.ok().data("item",video);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID修改课时")
    @PutMapping("update")
    public R update(@ApiParam(value = "课时对象",required = true)@RequestBody Video video){
        boolean update = videoService.updateById(video);
        if (update){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "课程ID",required = true)@PathVariable String id){
        //vod中删除视频文件
        videoService.removeMediaVideoById(id);

        boolean remove = videoService.removeById(id);
        if (remove){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }





}

