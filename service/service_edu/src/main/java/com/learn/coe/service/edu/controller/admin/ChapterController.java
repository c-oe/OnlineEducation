package com.learn.coe.service.edu.controller.admin;


import com.learn.coe.common.base.result.R;
import com.learn.coe.service.edu.entity.Chapter;
import com.learn.coe.service.edu.entity.vo.ChapterVo;
import com.learn.coe.service.edu.service.ChapterService;
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
@Api("章节管理")
@Slf4j
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R save(@ApiParam(value = "章节对象",required = true)@RequestBody Chapter chapter){
        boolean result = chapterService.save(chapter);
        if (result){
            return R.ok().message("保存成功");
        }else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据ID获取章节")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "章节ID",required = true)@PathVariable String id){
        Chapter chapter = chapterService.getById(id);
        if (chapter != null){
            return R.ok().data("item",chapter);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID修改章节")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "章节对象",required = true)@RequestBody Chapter chapter){
        boolean update = chapterService.updateById(chapter);
        if (update){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID删除章节")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "章节ID",required = true)@PathVariable String id){
        //删除课程视频
        videoService.removeMediaVideoByChapterId(id);
        //删除章节
        boolean remove = chapterService.removeChapterById(id);
        if (remove){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public R nestListByCourseId(@ApiParam(value = "课程ID",required = true)@PathVariable String courseId){
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("items",chapterVoList);
    }
}
