package cn.hfut.huangshan.utils;

/**
 * @author Administrator
 */
public class StringUtil {

    /**
     * @example date_week -> dateWeek
     * @param field 字段
     * @return 属性
     */
    public static String fieldToAttribute(String field){
        String result = "";
        switch (field){
            case "date_week": result = "dateWeek"; break;
            case "predict_num": result = "predictNum"; break;
            case "today_eight_num": result = "todayEightNum"; break;
            case "today_nine_num": result = "todayNineNum"; break;
            case "today_total_num": result = "todayTotalNum"; break;
            case "deviation_rate": result = "deviationRate"; break;
            case "order_num": result = "orderNum"; break;
            case "weather_name": result = "weatherName"; break;
            case "module_name": result = "moduleName"; break;
            case "description": result = "description"; break;
            case "is_holiday": result = "isHoliday"; break;
            case "holiday_name": result = "holidayName"; break;
            case "holiday_order": result = "holidayOrder"; break;
            case "pre1_total_num": result = "pre1TotalNum"; break;
            case "pre2_total_num": result = "pre2TotalNum"; break;
            case "pre3_total_num": result = "pre3TotalNum"; break;
            case "pre7_total_num": result = "pre7TotalNum"; break;
            case "date_name": result = "dateName"; break;
            default: result = "";
        }
        return result;
    }
}
