package com.learn.coe.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.coe.common.base.result.R;
import com.learn.coe.service.statistics.entity.Daily;
import com.learn.coe.service.statistics.feign.UcenterMemberService;
import com.learn.coe.service.statistics.mapper.DailyMapper;
import com.learn.coe.service.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Coffee
 * @since 2021-05-29
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {

        //当日统计信息已存在
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);

        //远程获取注册用户数的统计结果
        R r = ucenterMemberService.countRegisterNum(day);
        Integer registerNum = (Integer) r.getData().get("registerNum");
        Integer loginNum = (Integer) r.getData().get("registerNum");
        Integer videoViewNum = (Integer) r.getData().get("registerNum");
        Integer courseNum = (Integer) r.getData().get("registerNum");

        Daily daily = new Daily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {

        //学员登录数统计
        Map<String, Object> registerNum = getChartDataByType(begin, end, "register_num");
        //学员注册数统计
        Map<String, Object> loginNum = getChartDataByType(begin, end, "login_num");
        //课程播放数统计
        Map<String, Object> videoViewNum = getChartDataByType(begin, end, "video_view_num");
        //每日新增课程数统计
        Map<String, Object> courseNum = getChartDataByType(begin, end, "course_num");


        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("registerNum",registerNum);
        map.put("loginNum",loginNum);
        map.put("videoViewNum",videoViewNum);
        map.put("courseNum",courseNum);

        return map;
    }

    /**
     * 根据时间和要查询的列 查询数据
     * @param type 列名
     */
    private Map<String,Object> getChartDataByType(String begin,String end,String type){
        Map<String, Object> map = new HashMap<>();

        List<String> xList = new ArrayList<>();//日期列表
        List<Integer> yList = new ArrayList<>();//数据列表

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",type)
                .between("date_calculated",begin,end);

        List<Map<String, Object>> datamaps = baseMapper.selectMaps(queryWrapper);

        for (Map<String, Object> data : datamaps) {
            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);

            Integer count = (Integer) data.get(type);
            yList.add(count);
        }

        map.put("xData",xList);
        map.put("yList",yList);

        return map;
    }
}
