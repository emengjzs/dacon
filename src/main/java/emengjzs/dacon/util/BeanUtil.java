package emengjzs.dacon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

public class BeanUtil {

    public static String toString(Object o) {
        return JSON.toJSONString(o, SerializerFeature.PrettyFormat);
    }

    public static List<JSONObject> reduce(JSONArray array, String path) {
        return reduce(array, path, JSONObject.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> reduce(List array, String path, Class<T> clazz) {
        return (List<T>) JSONPath.eval(array, path);

    }


}
