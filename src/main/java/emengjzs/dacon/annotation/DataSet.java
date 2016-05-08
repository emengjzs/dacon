package emengjzs.dacon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSet {
    /**
     * 测试数据集
     *
     * @return
     */
    Data[] data();

    /**
     * key 前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 数据模板
     *
     * @return
     */
    String baseOn() default "";
}
