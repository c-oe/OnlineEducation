package com.learn.coe.service.cms.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.cms.entity.AdType;
import com.learn.coe.service.cms.service.AdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-23
 */
//@CrossOrigin
@Api("推荐位管理")
@Slf4j
@RestController
@RequestMapping("/admin/cms/ad-type")
public class AdTypeController {

    @Autowired
    private AdTypeService adTypeService;

    @ApiOperation("所有推荐类别列表")
    @GetMapping("list")
    public R listAll(){
        List<AdType> list = adTypeService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation("推荐类别分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码",required = true)@PathVariable Long page,
                      @ApiParam(value = "每页记录数",required = true)@PathVariable Long limit){
        Page<AdType> pageParam = new Page<>(page, limit);
        Page<AdType> pageModel = adTypeService.page(pageParam);
        List<AdType> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("根据ID删除推荐类别")
    @GetMapping("remove/{id}")
    public R removeById(@ApiParam(value = "推荐类别ID")@PathVariable String id){
        boolean remove = adTypeService.removeById(id);
        if (remove){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("新增推荐类别")
    @GetMapping("save")
    public R save(@ApiParam(value = "推荐类别对象",required = true)@RequestBody AdType adType){
        boolean save = adTypeService.save(adType);
        if (save){
            return R.ok().message("保存成功");
        }else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("更新推荐类别")
    @GetMapping("update")
    public R updateById(@ApiParam(value = "讲师推荐类别",required = true)@RequestBody AdType adType){
        boolean update = adTypeService.updateById(adType);
        if (update){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取推荐类别信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "推荐类别ID",required = true)@PathVariable String id){
        AdType adType = adTypeService.getById(id);
        if (adType != null){
            return R.ok().data("item",adType);
        }else {
            return R.error().message("数据不存在");
        }
    }

}

