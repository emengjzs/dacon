package emengjzs.dacon.config;

import emengjzs.dacon.annotation.PropertyConfiguration;
import emengjzs.dacon.util.TypeConvertService;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

public abstract class PropertiesConfigFactory {

    public static <T> T getInstance(Class<T> clazz, String fileName) throws Exception {
        return getInstance(clazz, fileName, "");
    }

    public static <T> T getInstance(Class<T> clazz, String fileName, String prefix) throws Exception {
        T ins = clazz.newInstance();
        ConversionService cs = TypeConvertService.getInstance();
        InputStream in = clazz.getClassLoader().getResourceAsStream(fileName);
        Properties prop = new Properties();
        prop.load(in);
        Set<String> keys = prop.stringPropertyNames();
        for (String key : keys) {
            try {
                Method m = ReflectionUtils.findMethod(clazz, "set" + getFieldName(key, prefix), null);
                if (m != null && m.getParameterTypes().length == 1) {
                    Class<?> targetType = m.getParameterTypes()[0];
                    if (cs.canConvert(String.class, targetType)) {
                        ReflectionUtils.invokeMethod(m, ins, cs.convert(prop.getProperty(key), targetType));
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return ins;
    }

    public static <T> T getInstance(Class<T> clazz) throws Exception {
        T ins = clazz.newInstance();
        PropertyConfiguration anno = clazz.getAnnotation(PropertyConfiguration.class);
        if (anno != null) {
            String fileName = anno.value();
            String prefix = anno.prefix();
            return getInstance(clazz, fileName, prefix);
        }
        return ins;
    }

    private static String getFieldName(String propertyKey, String prefix) {
        StringBuilder sb = new StringBuilder(propertyKey.length());
        char[] chars = propertyKey.toCharArray();

        int beginCharIndex = 0;
        if (StringUtils.isNotEmpty(prefix) && propertyKey.startsWith(prefix + ".")) {
            beginCharIndex = prefix.length() + 1;
        }

        sb.append(Character.toUpperCase(chars[beginCharIndex]));

        for (int i = beginCharIndex + 1; i < propertyKey.length(); i++) {
            if (chars[i] == '.' && i + 1 < chars.length) {
                sb.append(Character.toUpperCase(chars[++i]));
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

}
