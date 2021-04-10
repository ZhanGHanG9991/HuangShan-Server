package cn.hfut.huangshan.mapper;

import cn.hfut.huangshan.pojo.DailyStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */

@Repository
@Mapper
public interface DailyStatisticsMapper {
    /**
     * 限制性全查询：降序前1000条
     */
    List<DailyStatistics> getLimitAllDailyStatistics();

    /**
     * 按开始日期和结束日期查询
     */
    List<DailyStatistics> getPeriodDailyStatistics(@Param("startTime") String startTime, @Param("endTime")String endTime);

    /**
     * 根据日期查询某一天的
     */
    DailyStatistics getOneStatisticsByDate(@Param("date") String date);
}
