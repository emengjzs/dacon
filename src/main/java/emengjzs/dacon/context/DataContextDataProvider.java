package emengjzs.dacon.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import emengjzs.dacon.annotation.Data;
import emengjzs.dacon.annotation.DataSet;
import emengjzs.dacon.annotation.RequestBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataContextDataProvider {
    private static final Object[][] DUMMY_OBJECT = new Object[1][0];

    private DataContext dataContext;

    public DataContextDataProvider() {

    }

    public Object[][] getData(Method m) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {
        try {
            if (m.getParameterTypes().length == 0) {
                return DUMMY_OBJECT;
            }

            DataSet dataSet = m.getAnnotation(DataSet.class);
            if (null != dataSet) {
                List<Object[]> provide;
                provide = new ArrayList<Object[]>(dataSet.data().length);
                for (Data data : dataSet.data()) {
                    provide.add(processDataAnnotation(data, m, new DataSetMetaData(dataSet)));
                }
                return provide.toArray(DUMMY_OBJECT);
            }
            Data data = m.getAnnotation(Data.class);
            if (null == data) {
                Annotation[] annos = m.getParameterAnnotations()[0];
                for (Annotation a : annos) {
                    if (a.annotationType().equals(Data.class)) {
                        return new Object[][]{processDataAnnotation((Data) a, m, new DataSetMetaData())};
                    }
                }
                return new Object[][]{processDataAnnotation(null, m, new DataSetMetaData())};

            }
            return new Object[][]{processDataAnnotation(data, m, new DataSetMetaData())};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getData(String value, String baseOn) {
        if (!StringUtils.isEmpty(baseOn)) {
            return merge(baseOn, value);
        } else {
            return dataContext.getJSONObjectData(value);
        }
    }

    private Object[] processDataAnnotation(Data data, Method m, DataSetMetaData meta) throws IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException {
        Object o = null;
        JSON resultData = null;
        List<Object> params = new ArrayList<Object>(2);
        if (data != null) {
            String value = meta.prefix + data.value();
            String baseOn = StringUtils.isEmpty(data.baseOn())
                    ? meta.baseOn : data.baseOn();
            if (!StringUtils.isEmpty(baseOn)) {
                resultData = getData(value, baseOn);
                o = transform(resultData, data.proto(), m.getParameterTypes()[0]);
            } else {
                o = this.dataContext.getData(value, m.getParameterTypes()[0]);
            }
        }
        params.add(o);
        Annotation[][] annos = m.getParameterAnnotations();


        for (int i = 1, len = annos.length; i < len; ++i) {
            for (Annotation a : annos[i]) {
                if (a.annotationType().equals(RequestBean.class)) {
                    params.add(JSON.toJavaObject(resultData, m.getParameterTypes()[i]));
                } else if (a.annotationType().equals(Data.class)) {
                    Data paramMetaData = (Data) a;
                    if (meta.hasBaseOn()) {
                        JSON paramData = getData(paramMetaData.value(), paramMetaData.baseOn());
                        o = transform(paramData, paramMetaData.proto(), m.getParameterTypes()[i]);
                    } else {
                        o = this.dataContext.getData(paramMetaData.value(), m.getParameterTypes()[i]);
                    }
                    params.add(o);
                } else {
                    params.add(null);
                }
            }
        }
        return params.toArray();
    }

    public void setDataContext(DataContext dataContext) {
        this.dataContext = dataContext;
    }


    private Object transform(JSON result, boolean isProto, Class<?> clazz) throws IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException {
        Object o = JSON.toJavaObject(result, clazz);
        if (isProto) {
            return BeanUtils.cloneBean(o);
        }
        return o;
    }

    private JSONObject merge(String based, String changed) {
        JSONObject tplData = (JSONObject) dataContext.getJSONObjectData(based).clone();
        tplData.putAll(dataContext.getJSONObjectData(changed));
        return tplData;
    }

    class DataSetMetaData {
        String prefix;
        String baseOn;

        DataSetMetaData() {
            prefix = "";
            baseOn = "";
        }

        DataSetMetaData(DataSet dataSet) {
            this.baseOn = dataSet.baseOn();
            this.prefix = dataSet.prefix();
        }

        boolean hasBaseOn() {
            return !this.baseOn.equals("");
        }

    }
}
