package cn.hfut.huangshan.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author Administrator
 */
public class Json2MapUtil {

    //map转java对象
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        String jsonStr = JSONObject.toJSONString(map);
        return JSONObject.parseObject(jsonStr, beanClass);
    }

    //java对象转map
    public static Map<String, Object> objectToMap(Object obj) {
        String jsonStr = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(jsonStr);
    }

}
