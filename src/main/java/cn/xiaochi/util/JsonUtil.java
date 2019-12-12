package cn.xiaochi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gsonb包
 * json格式化
 */
public class JsonUtil {

    /**
     * 格式化输出数据
     * @param object
     * @return
     */
    public static String toJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
