package com.learn.coe.service.statistics.controller.admin;


import com.learn.coe.common.base.result.R;
import com.learn.coe.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Coffee
 * @since 2021-05-29
 */
@Api("统计分析管理")
@RestController
@RequestMapping("/statistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("create/{day}")
    public R createStatisticsByDay(@ApiParam(value = "日期",required = true)@PathVariable String day){
        dailyService.createStatisticsByDay(day);
        return R.ok().message("生成成功");
    }

    @ApiOperation("生成统计记录")
    @GetMapping("show-chart/{begin}/{end}")
    public R showChart(@ApiParam("开始时间")@PathVariable String begin,
                       @ApiParam("结束时间")@PathVariable String end){
        Map<String, Map<String, Object>> map = dailyService.getChartData(begin,end);
        return R.ok().data("chartData",map);
    }
}

