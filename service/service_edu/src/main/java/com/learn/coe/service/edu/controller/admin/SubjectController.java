package com.learn.coe.service.edu.controller.admin;


import com.learn.coe.common.base.result.R;
import com.learn.coe.common.base.result.ResultCodeEnum;
import com.learn.coe.common.base.util.ExceptionUtils;
import com.learn.coe.service.base.exception.CoeException;
import com.learn.coe.service.edu.entity.vo.SubjectVo;
import com.learn.coe.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-13
 */
//@CrossOrigin
@Api("课程分类管理")
@Slf4j
@RestController
@RequestMapping("/admin/edu/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("Excel批量导入课程分类")
    @PostMapping("import")
    public R batchImport(@RequestParam("file")MultipartFile file){
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new CoeException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("嵌套数据列表")
    @PostMapping("nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items",subjectVoList);
    }
}

