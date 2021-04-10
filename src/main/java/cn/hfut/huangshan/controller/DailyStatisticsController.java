package cn.hfut.huangshan.controller;

import cn.hfut.huangshan.constants.ErrorCode;
import cn.hfut.huangshan.pojo.DailyNum;
import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.response.ResultObj;
import cn.hfut.huangshan.service.DailyStatisticsService;
import cn.hfut.huangshan.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("daily_statistics")
public class DailyStatisticsController {

    @Autowired
    DailyStatisticsService dailyStatisticsService;

    /**
     * 限制性全查询：降序前1000条
     *
     * @return
     */
    @RequestMapping(value = "/limit_all", method = RequestMethod.GET)
    public ResultObj getLimitAllDailyStatistics() {
        List<DailyStatistics> dailyStatistics = dailyStatisticsService.getLimitAllDailyStatistics();
        if (dailyStatistics.size() > 0) {
            return ResponseUtil.success(dailyStatistics);
        } else {
            return ResponseUtil.error(ErrorCode.QUERY_FAIL, ErrorCode.QUERY_FAIL_MSG, null);
        }
    }

    /**
     * 按开始日期和结束日期查询
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return
     */
    @RequestMapping(value = "/period/start/{start}/end/{end}", method = RequestMethod.GET)
    public ResultObj getPeriodDailyNum(@PathVariable("start") String start, @PathVariable("end") String end) {
        //这里让前端来处理开始时间要写于结束时间的校验
        List<DailyStatistics> dailyStatistics = dailyStatisticsService.getPeriodDailyNum(start, end);
        if (dailyStatistics.size() > 0) {
            return ResponseUtil.success(dailyStatistics);
        } else {
            return ResponseUtil.error(ErrorCode.QUERY_FAIL, ErrorCode.QUERY_FAIL_MSG, null);
        }
    }

    /**
     * 根据日期查询某一天的
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public ResultObj getOneStatisticsByDate(@PathVariable("date") String date) {
        DailyStatistics dailyStatistics = dailyStatisticsService.getOneStatisticsByDate(date);
        if (dailyStatistics != null) {
            return ResponseUtil.success(dailyStatistics);
        } else {
            return ResponseUtil.error(ErrorCode.QUERY_FAIL, ErrorCode.QUERY_FAIL_MSG, null);
        }
    }


}
