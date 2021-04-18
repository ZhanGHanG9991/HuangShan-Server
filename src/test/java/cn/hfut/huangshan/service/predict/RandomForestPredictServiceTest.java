package cn.hfut.huangshan.service.predict;

import cn.hfut.huangshan.mapper.DailyStatisticsMapper;
import cn.hfut.huangshan.pojo.DailyStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RandomForestPredictServiceTest {

    @Autowired
    RandomForestPredictService randomForestPredictService;

    @Autowired
    DailyStatisticsMapper dailyStatisticsMapper;

    @Test
    void modelTrainer() {
        randomForestPredictService.modelTrainer();
    }

    @Test
    void predictor() {
        List<DailyStatistics> dailyStatisticsList = randomForestPredictService.predictor(dailyStatisticsMapper.getPeriodDailyStatistics("2019-11-01", "2019-11-05"));

    }

    @Test
    void modifyPredicts(){
        double sum = 0;
        ArrayList<Double> arr = new ArrayList<>();
        List<DailyStatistics> dailyStatisticsList = randomForestPredictService.predictor(dailyStatisticsMapper.getAllDailyStatistics());
        for(DailyStatistics element : dailyStatisticsList){
            element.setDeviationRate((double) (element.getPredictNum() - element.getTodayTotalNum()) / element.getTodayTotalNum());
            arr.add(element.getDeviationRate());
            sum += element.getDeviationRate() > 0 ? element.getDeviationRate() : -element.getDeviationRate();
            Integer j = dailyStatisticsMapper.updateOne(element);
        }
        System.out.println(sum / dailyStatisticsList.size());
    }
}