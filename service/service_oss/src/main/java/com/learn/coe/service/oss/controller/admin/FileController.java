package com.learn.coe.service.oss.controller.admin;

import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.ExceptionUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author coffee
 * @Date 2021-05-15 16:57
 */
@Api("阿里云文件管理")
@RestController
//@CrossOrigin
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;


    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file,
                    @ApiParam(value = "模块",required = true) @RequestParam("module") String module) {
        String upload;
        try {
            upload = fileService.upload(file.getInputStream(), module, file.getOriginalFilename());

            return R.ok().message("文件上传成功").data("url",upload);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }

    }

    @ApiOperation("文件删除")
    @DeleteMapping("remove")
    public R removeFile(@ApiParam(value = "要删除文件的url路径",required = true)@RequestBody String url){
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }



    @ApiOperation("测试")
    @GetMapping("test")
    public R test(){
        log.info("oss test被调用");
        return R.ok();
    }
}
