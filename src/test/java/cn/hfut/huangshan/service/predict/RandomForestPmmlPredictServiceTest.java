package cn.hfut.huangshan.service.predict;

import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.service.DailyStatisticsService;
import cn.hfut.huangshan.service.predict.RandomForestPmmlPredictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class RandomForestPmmlPredictServiceTest {

    @Autowired
    DailyStatisticsService dailyStatisticsService;

    @Autowired
    RandomForestPmmlPredictService randomForestPmmlPredictService;

    @Test
    void predictTest(){
        DailyStatistics dailyStatistics = dailyStatisticsService.getOneStatisticsByDate("2019-11-21");
        DailyStatistics dailyStatisticsPredict = randomForestPmmlPredictService.predict(dailyStatistics);
        System.out.println(dailyStatisticsPredict.toString());
    }

    @Test
    void updateDeviation(){
        List<DailyStatistics> dailyStatisticsList = dailyStatisticsService.getAllDailyStatistics();
        for(DailyStatistics dailyStatistics : dailyStatisticsList){
            DailyStatistics dailyStatisticsPredict = randomForestPmmlPredictService.predict(dailyStatistics);
            dailyStatisticsPredict.setDeviationRate((double) (dailyStatisticsPredict.getPredictNum() - dailyStatisticsPredict.getTodayTotalNum()) / dailyStatisticsPredict.getTodayTotalNum());
            dailyStatisticsService.updateOne(dailyStatisticsPredict);
        }
    }

}