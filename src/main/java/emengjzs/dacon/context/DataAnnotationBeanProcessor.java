package emengjzs.dacon.context;

import com.alibaba.fastjson.JSONObject;
import emengjzs.dacon.annotation.Data;
import emengjzs.dacon.annotation.DataConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class DataAnnotationBeanProcessor implements BeanPostProcessor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private DataContext dataContext;

    public DataAnnotationBeanProcessor() {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        processToBeInjectedData(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotation(DataConfiguration.class) != null) {
            processDataConfiguration(bean, beanName);
        }
        return bean;
    }


    protected void processDataConfiguration(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        for (Method m : ReflectionUtils.getAllDeclaredMethods(clazz)) {
            Data anno;
            if ((anno = m.getAnnotation(Data.class)) != null) {
                dataContext.getAll().put(anno.value(), ReflectionUtils.invokeMethod(m, bean));
            }
        }
    }

    protected void processToBeInjectedData(Object bean, String beanName) {
        for (Field f : bean.getClass().getDeclaredFields()) {

            try {
                Data d;
                if ((d = AnnotationUtils.getAnnotation(f, Data.class)) != null) {
                    String baseOn = d.baseOn();
                    Object o;
                    if (StringUtils.isNotEmpty(baseOn)) {
                        o = this.dataContext.getData(d.value(), baseOn, f.getType());

                    } else {
                        o = this.dataContext.getData(d.value(), f.getType());
                        if (d.proto() && f.getType().equals(JSONObject.class)) {
                            o = ((JSONObject) o).clone();
                        }
                    }


                    ReflectionUtils.makeAccessible(f);
                    if (f.get(bean) != null) {
                        logger.warn("Bean {}.{} is not null! It will be overrided by Data {}",
                                beanName, f.getName(), d.value());
                    }

                    logger.debug("Inject Data [{}] to {}.{}", d.value(), beanName, f.getName());
                    f.set(bean, o);
                }
            } catch (Exception e) {
                throw new BeanCreationException("Data inject error! Bean: "
                        + beanName + "Field: " + f.getName(), e);
            }
        }
    }


    public void setDataContext(DataContext dataContext) {
        logger.debug("DataAnnotationBeanProcessor handles @Data base on [{}]", dataContext.getDataContrextConfigName());
        this.dataContext = dataContext;
    }
}
