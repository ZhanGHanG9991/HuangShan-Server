package cn.hfut.huangshan.service;

import cn.hfut.huangshan.mapper.DailyStatisticsMapper;
import cn.hfut.huangshan.pojo.DailyNum;
import cn.hfut.huangshan.pojo.DailyStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class DailyStatisticsService {

    @Autowired
    DailyStatisticsMapper dailyStatisticsMapper;

    /**
     * 限制性全查询：降序前1000条
     * @return
     */
    @Transactional
    public List<DailyStatistics> getLimitAllDailyStatistics(){
        return dailyStatisticsMapper.getLimitAllDailyStatistics();
    }

    /**
     * 按开始日期和结束日期查询
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    @Transactional
    public List<DailyStatistics> getPeriodDailyNum(String startTime, String endTime){
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
        return dailyStatisticsMapper.getPeriodDailyStatistics(formatStartTime,formatEndTime);
    }

    /**
     * 根据日期查询某一天的
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

}
