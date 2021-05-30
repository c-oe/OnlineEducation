package com.learn.coe.service.statistics.task;

import com.learn.coe.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author coffee
 * @since 2021-05-29 14:43
 */
@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    /**
     * 定时生成统计记录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void taskGenStatisticsData(){

        //生成前一天的日期
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");

        dailyService.createStatisticsByDay(day);
    }
}
