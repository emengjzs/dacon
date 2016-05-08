package emengjzs.dacon.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import emengjzs.dacon.context.tool.ExtDateTool;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataContext {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private JSONObject map;

    @Autowired
    private VelocityEngine velocityEngine;

    private Map<String, Object> params = new ConcurrentHashMap<String, Object>();

    private String dataContrextConfigName = "dataContext.json";

    public DataContext() {

    }

    public DataContext(String dataContrextConfigName) {
        this.dataContrextConfigName = dataContrextConfigName;
    }

    public String getDataContrextConfigName() {
        return dataContrextConfigName;
    }

    @PostConstruct
    public void setDataConfiguration() {
        params.put("date", new ExtDateTool());
        String sb = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, dataContrextConfigName,
                "utf-8", params);
        logger.debug("*****DataContext after parsed by vm******\n");
        // map = new JSONObject(true);
        map = (JSON.parseObject(sb, Feature.OrderedField));
        // map = JSON.parseObject(sb, Feature.SortFeidFastMatch);
        logger.debug(JSON.toJSONString(map, true));
    }

    @Deprecated
    public Map<String, Object> getAll() {
        return map;
    }

    public Map<String, Object> getMapData(String name) {
        Map<String, Object> r = map.getJSONObject(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }

    /**
     * 得到构造数据，以base为基础，返回是以base数据为基础，与name数据合并之后的结果 在测试中，涉及测试项的参数往往只是业务参数中的一部分，此可以减少业务数据字典 变化对测试的影响 注意：仅为浅合并
     *
     * @param name 数据名key
     * @param base 数据tpl
     * @return 构造数据
     */
    public JSONObject getJSONObjectData(String name, String base) {
        JSONObject r = getJSONObjectData(name);
        JSONObject b = (JSONObject) getJSONObjectData(base).clone();
        b.putAll(r);
        return b;
    }

    /**
     * 得到构造数据，以base为基础，返回是以base数据为基础，与bean数据合并之后的结果 在测试中，涉及测试项的参数往往只是业务参数中的一部分，此可以减少业务数据字典 变化对测试的影响 注意：仅为浅合并
     *
     * @param bean 数据
     * @param base 数据tpl
     * @return 构造数据
     */
    public JSONObject getJSONObjectData(Object bean, String base) {
        JSONObject b = (JSONObject) getJSONObjectData(base).clone();
        b.putAll((JSONObject) JSON.toJSON(bean));
        return b;
    }

    public JSONObject getJSONObjectData(String name) {
        JSONObject r = map.getJSONObject(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }


    public Long getIntData(String name) {
        Long r = map.getLong(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }

    public String getStringData(String name) {
        String r = map.getString(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }

    public <T> List<T> getListData(String name, Class<T> clazz) {
        JSONArray r = map.getJSONArray(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return JSON.parseArray(r.toJSONString(), clazz);
    }


    public <T> T getData(String name, String base, Class<T> type) {
        return JSON.toJavaObject(getJSONObjectData(name, base), type);
    }

    public Object getData(String name) {

        Object r = map.get(name);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }

    public <T> T getData(String name, Class<T> type) {
        T r = map.getObject(name, type);
        if (null == r) {
            throw new NoSuchDataException(name);
        }
        return r;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
