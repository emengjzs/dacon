package emengjzs.dacon.config;

import emengjzs.dacon.annotation.PropertyConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by emengjzs on 2016/4/15.
 */
public class PropertiesConfigurationBeanProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (bean.getClass().getAnnotation(PropertyConfiguration.class) != null) {
                return PropertiesConfigFactory.getInstance(bean.getClass());
            }

        } catch (Exception e) {
            return bean;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
