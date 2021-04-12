package cn.hfut.huangshan.service;

import cn.hfut.huangshan.pojo.DailyStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DailyStatisticsServiceTest {

    @Autowired
    DailyStatisticsService dailyStatisticsService;

    @Test
    public void addOneDailyStatisticsTest(){
        DailyStatistics dailyStatistics = new DailyStatistics(
                "2019-11-17",
                "星期日",
                0,
                0,
                0,
                -1,
                0,
                0,
                "小雨",
                null,
                null,
                null,
                null,
                0,
                16871,
                0,
                7200,
                10466);
//        DailyStatistics predict = dailyStatisticsService.addOneDailyStatistics(dailyStatistics);
//        System.out.println(predict.toString());
    }

}