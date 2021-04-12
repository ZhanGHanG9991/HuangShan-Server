package cn.hfut.huangshan.controller;

import cn.hfut.huangshan.constants.ErrorCode;
import cn.hfut.huangshan.pojo.DailyNum;
import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.response.ResultObj;
import cn.hfut.huangshan.service.DailyStatisticsService;
import cn.hfut.huangshan.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 全查询：降序
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResultObj getAllDailyStatistics() {
        List<DailyStatistics> dailyStatistics = dailyStatisticsService.getAllDailyStatistics();
        if (dailyStatistics.size() > 0) {
            return ResponseUtil.success(dailyStatistics);
        } else {
            return ResponseUtil.error(ErrorCode.QUERY_FAIL, ErrorCode.QUERY_FAIL_MSG, null);
        }
    }

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

    /**
     * 插入一条记录
     * @param dailyStatistics
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultObj addOneDailyStatistics(@RequestBody DailyStatistics dailyStatistics){
        boolean isSuccess = dailyStatisticsService.addOneDailyStatistics(dailyStatistics);
        if (isSuccess){
            DailyStatistics insertedData = dailyStatisticsService.getOneStatisticsByDate(dailyStatistics.getDateName());
            return ResponseUtil.success(insertedData);
        }else {
            return ResponseUtil.error(ErrorCode.ADD_FAIL,ErrorCode.ADD_FAIL_MSG, null);
        }
    }

    /**
     * 更新一个
     * @param date
     * @param dailyStatistics
     * @return
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.PUT)
    public ResultObj updateOne(@PathVariable("date") String date, @RequestBody DailyStatistics dailyStatistics){
        boolean isSuccess = dailyStatisticsService.updateOne(dailyStatistics);
        if (isSuccess){
            DailyStatistics insertedData = dailyStatisticsService.getOneStatisticsByDate(date);
            return ResponseUtil.success(insertedData);
        }else {
            return ResponseUtil.error(ErrorCode.UPDATE_FAIL,ErrorCode.UPDATE_FAIL_MSG, null);
        }
    }

    /**
     * 删除一个
     * @param date
     * @return
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.DELETE)
    public ResultObj deleteOne(@PathVariable("date") String date){
        boolean isSuccess = dailyStatisticsService.deleteOne(date);
        if (isSuccess){
            return ResponseUtil.success(null);
        }else {
            return ResponseUtil.error(ErrorCode.DELETE_FAIL,ErrorCode.DELETE_FAIL_MSG, null);
        }
    }


}
