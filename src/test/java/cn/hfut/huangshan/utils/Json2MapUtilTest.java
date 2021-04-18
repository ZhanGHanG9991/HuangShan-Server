package cn.hfut.huangshan.utils;

import cn.hfut.huangshan.pojo.DailyStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Json2MapUtilTest {

    @Test
    void objectToMap() {

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

        System.out.println(Json2MapUtil.objectToMap(dailyStatistics));


    }
}