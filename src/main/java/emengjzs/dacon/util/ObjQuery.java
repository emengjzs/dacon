package emengjzs.dacon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import java.util.ArrayList;
import java.util.List;

/**
 * OQL 简单封装 参考 JSONPath
 *
 * @author jiaozishun
 */
public class ObjQuery {

    private Object o;

    private List<String> oqls = new ArrayList<String>();

    private ObjQuery(Object o) {
        this.o = o;
    }

    public static ObjQuery from(Object o) {
        return new ObjQuery(o);
    }

    public ObjQuery eval(String path) {
        oqls.add(path);
        return this;
    }

    public <T> List<T> list() {
        for (String oql : oqls) {
            o = JSONPath.eval(o, oql);
        }
        return (List<T>) o;
    }

    @SuppressWarnings("unchecked")
    public <T> T uniqueResult(Class<T> clazz) {
        return (T) uniqueResult();
    }

    public Object uniqueResult() {
        for (String oql : oqls) {
            o = JSONPath.eval(o, oql);
            System.out.println("result: " + JSON.toJSONString(o));
        }

        if (o instanceof List) {
            return ((List) o).isEmpty() ? null : ((List) o).get(0);
        } else {
            return o;
        }
    }

    public String uniqueString() {
        return (String) uniqueResult();
    }

    public int uniqueInt() {
        return (Integer) uniqueResult();
    }

    public Long uniqueLong() {
        return new Long((Integer) uniqueResult());
    }
}
