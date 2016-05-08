package emengjzs.dacon.context;

import emengjzs.dacon.annotation.DynamicData;
import emengjzs.dacon.annotation.DynamicDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class DynamicDataProviderProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    public DynamicDataProviderProcessor() {
        // TODO Auto-generated constructor stub
    }


    public void process(Method m, Object[] params) {
        if (params.length == 0) {
            return;
        } else {
            DynamicDataProvider anno = null;
            if ((anno = m.getAnnotation(DynamicDataProvider.class)) != null) {
                ProviderMeta meta = new ProviderMeta(anno);
                Annotation[][] paraAnnos = m.getParameterAnnotations();
                for (int i = 0; i < params.length; ++i) {
                    DynamicData data = null;
                    if ((data = getAnnotationParameter(DynamicData.class, paraAnnos[i])) != null) {
                        meta.injectData(data, params, i);
                    }
                }
            }
        }
    }

    private <T extends Annotation> T getAnnotationParameter(Class<T> clazz, Annotation[] annos) {
        for (Annotation a : annos) {
            if (a.annotationType().equals(clazz)) {
                return (T) a;
            }
        }
        return null;
    }

    class ProviderMeta {
        Class<?> defaultProviderClass;

        public ProviderMeta(DynamicDataProvider anno) {
            this.defaultProviderClass = anno.value();
        }

        public Class<?> getProviderClass(DynamicData data) {
            return data.clazz() == Object.class ? defaultProviderClass : data.clazz();
        }

        public Object getData(DynamicData data) {
            Class<?> clazz = getProviderClass(data);
            return ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(clazz, data.value()),
                    applicationContext.getBean(clazz));
        }

        public void injectData(DynamicData data, Object[] params, int index) {
            params[index] = getData(data);
        }
    }

}
