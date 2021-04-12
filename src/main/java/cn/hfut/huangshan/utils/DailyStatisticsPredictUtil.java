package cn.hfut.huangshan.utils;

/**
 * @author Administrator
 */
public class DailyStatisticsPredictUtil {

    public static int dateWeekToNum(String dateWeek){
        int num;
        switch (dateWeek){
            case "星期一": num = 1; break;
            case "星期二": num = 2; break;
            case "星期四": num = 4; break;
            case "星期五": num = 5; break;
            case "星期六": num = 6; break;
            case "星期日": num = 7; break;
            default: num = 3;
        }
        return num - 1;
    }

    public static int weatherTypeToNum(String weatherName){
        int num;
        switch (weatherName){
            case "晴间多云": num = 2; break;
            case "多云": num = 3; break;
            case "阴": num = 4; break;
            case "小雨": num = 5; break;
            case "中雨": num = 6; break;
            case "大雨": num = 7; break;
            case "暴雨": num = 8; break;
            case "阵雨": num = 9; break;
            case "雷阵雨": num = 10; break;
            case "强阵雨": num = 11; break;
            case "小雪": num = 12; break;
            case "中雪": num = 13; break;
            case "大雪": num = 14; break;
            case "雨夹雪": num = 15; break;
            case "暴雪": num = 16; break;
            default: num = 1;
        }
        return num - 1;
    }
}
