package cn.hfut.huangshan.service;

import cn.hfut.huangshan.mapper.DailyStatisticsMapper;
import cn.hfut.huangshan.pojo.DailyNum;
import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.service.predict.RandomForestPmmlPredictService;
import cn.hfut.huangshan.service.predict.RandomForestPredictService;
import cn.hfut.huangshan.utils.DateCalculateUtil;
import cn.hfut.huangshan.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class DailyStatisticsService {

    @Autowired
    DailyStatisticsMapper dailyStatisticsMapper;

    @Autowired
    RandomForestPmmlPredictService randomForestPmmlPredictService;

    /**
     * 全查询：降序
     */
    @Transactional
    public List<DailyStatistics> getAllDailyStatistics() {
        return dailyStatisticsMapper.getAllDailyStatistics();
    }

    /**
     * 限制性全查询：降序前1000条
     *
     * @return
     */
    @Transactional
    public List<DailyStatistics> getLimitAllDailyStatistics() {
        return dailyStatisticsMapper.getLimitAllDailyStatistics();
    }

    /**
     * 按开始日期和结束日期查询
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    @Transactional
    public List<DailyStatistics> getPeriodDailyNum(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(startTime);
            date2 = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatStartTime = format.format(date1);
        String formatEndTime = format.format(date2);
        return dailyStatisticsMapper.getPeriodDailyStatistics(formatStartTime, formatEndTime);
    }

    /**
     * 根据日期查询某一天的
     *
     * @param date
     * @return
     */
    public DailyStatistics getOneStatisticsByDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatDate = format.format(date1);
        return dailyStatisticsMapper.getOneStatisticsByDate(formatDate);
    }

    /**
     * 插入一个
     * 参数均由前端传入
     *
     * @param dailyStatistics
     * @return
     */
    @Transactional
    public Boolean addOneDailyStatistics(DailyStatistics dailyStatistics){

        String date = DateFormatUtil.toDate(dailyStatistics.getDateName());
        dailyStatistics.setDateName(date);

        String pre1Date = DateCalculateUtil.getDistanceDay(date, -1);
        String pre2Date = DateCalculateUtil.getDistanceDay(date, -2);
        String pre3Date = DateCalculateUtil.getDistanceDay(date, -3);
        String pre7Date = DateCalculateUtil.getDistanceDay(date, -7);

        List<DailyStatistics> dailyStatisticsList = dailyStatisticsMapper.getPeriodDailyStatistics(pre7Date, pre1Date);

        for (DailyStatistics element : dailyStatisticsList) {
            if (element.getDateName().equals(pre1Date)) {
                dailyStatistics.setPre1TotalNum(element.getTodayTotalNum());
                break;
            }
        }

        for (DailyStatistics item : dailyStatisticsList) {
            if (item.getDateName().equals(pre2Date)) {
                dailyStatistics.setPre2TotalNum(item.getTodayTotalNum());
                break;
            }
        }

        for (DailyStatistics value : dailyStatisticsList) {
            if (value.getDateName().equals(pre3Date)) {
                dailyStatistics.setPre3TotalNum(value.getTodayTotalNum());
                break;
            }
        }

        for (DailyStatistics statistics : dailyStatisticsList) {
            if (statistics.getDateName().equals(pre7Date)) {
                dailyStatistics.setPre7TotalNum(statistics.getTodayTotalNum());
                break;
            }
        }

        DailyStatistics dailyStatisticsPredict = randomForestPmmlPredictService.predict(dailyStatistics);

        // 计算偏差
        double rate = (double) (dailyStatisticsPredict.getPredictNum() - dailyStatisticsPredict.getTodayTotalNum()) / dailyStatisticsPredict.getTodayTotalNum();
        dailyStatisticsPredict.setDeviationRate(rate);

        Integer rows = dailyStatisticsMapper.addOneDailyStatistics(dailyStatisticsPredict);
        return rows > 0;
    }

    /**
     * 更新一个
     *
     * @param dailyStatistics
     * @return
     */
    @Transactional
    public Boolean updateOne(DailyStatistics dailyStatistics) {
        String date = DateFormatUtil.toDate(dailyStatistics.getDateName());
        dailyStatistics.setDateName(date);

        // 计算偏差
        double rate = (double) (dailyStatistics.getTodayTotalNum() - dailyStatistics.getPredictNum()) / dailyStatistics.getTodayTotalNum();
        dailyStatistics.setDeviationRate(rate);

        Integer rows = dailyStatisticsMapper.updateOne(dailyStatistics);
        return rows > 0;
    }

    /**
     * 删除一个
     * @param date
     * @return
     */
    @Transactional
    public boolean deleteOne(String date) {
        Integer rows = dailyStatisticsMapper.deleteOne(date);
        return rows > 0;
    }

}
