package emengjzs.dacon.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.util.ArrayList;
import java.util.List;

public class JSONResponse {

    private JSONObject res;

    public JSONResponse(JSONObject res) {
        this.res = res;
    }

    public String getJsonStr() {
        return res.toJSONString();
    }

    public boolean isSuccess() {
        return this.res.getBoolean("success");
    }

    public String getMessage() {
        return this.res.getString("message");
    }

    public JSONArray getPageArrayResult() {
        return this.res.getJSONObject("page").getJSONArray("result");
    }

    public <T> List<T> getPageArrayResult(Class<T> clazz) {

        JSONArray array = this.res.getJSONObject("page").getJSONArray("result");
        if (null == array) {
            return null;
        }
        List<T> beanList = new ArrayList<T>();
        for (int i = 0, len = array.size(); i < len; i++) {
            beanList.add(array.getObject(i, clazz));
        }
        return beanList;
    }

    public JSONObject getResult() {
        return this.res.getJSONObject("result");
    }

    public <T> T getResult(Class<T> clazz) {
        return this.res.getObject("result", clazz);
    }

    public JSONArray getArrayResult() {
        return this.res.getJSONArray("result");
    }

    public <T> List<T> getArrayResult(Class<T> clazz) {

        JSONArray array = this.res.getJSONArray("result");
        List<T> beanList = new ArrayList<T>(array.size());
        for (int i = 0, len = array.size(); i < len; i++) {
            beanList.add(array.getObject(i, clazz));
        }
        return beanList;
    }

    public JSONObject getJSON() {
        return res;
    }

    @SuppressWarnings("unchecked")
    public <T> T getJSONValue(String path, Class<T> t) {
        return (T) JSONPath.eval(this.res, path);
    }
}
