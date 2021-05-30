package com.learn.coe.service.vod.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.ExceptionUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author coffee
 * @since 2021-05-19 22:31
 */
@Api("阿里云视频点播")
//@CrossOrigin
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("上传视频")
    @PostMapping("upload")
    public R uploadVideo(@ApiParam(value = "文件",required = true)
                             @RequestParam("file")MultipartFile file){
        try {
            String videoId = videoService.uploadVideo(file.getInputStream(), file.getOriginalFilename());
            return R.ok().message("视频上传成功").data("video",videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation("删除视频")
    @DeleteMapping("remove/{vodId}")
    public R removeVideo(@ApiParam(name = "vodId",value = "阿里云视频Id",required = true)
                         @PathVariable String vodId){
        try {
            videoService.removeVideo(vodId);
            return R.ok().message("视频删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }


    @ApiOperation("批量删除视频")
    @DeleteMapping("remove")
    public R removeVideoByIdList(@ApiParam(value = "阿里云视频Id列表",required = true)
                         @RequestBody List<String> vodIdList){
        try {
            videoService.removeVideoByIdList(vodIdList);
            return R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }



}
