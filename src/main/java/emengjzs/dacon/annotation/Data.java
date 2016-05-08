package emengjzs.dacon.annotation;

import emengjzs.dacon.context.DataAnnotationBeanProcessor;
import emengjzs.dacon.context.DataContext;
import emengjzs.dacon.testng.AbstractDataContextTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test 数据注解，标明的value值作为key从DataContext 中 寻找数据并注入测试方法的第一个参数中(方法注解）或方法参数（参数注解） 或成员参数中（成员变量注解）. 默认注入类型为
 * JSONObject，自动根据入参类型转换
 * <p>
 * 方法的注入和方法参数的注入 仅能在Test类中使用，并声明@Test(dataProvider="dataContext")
 * </p>
 * <p>
 * 成员参数的注入需要添加 {@link DataAnnotationBeanProcessor DataAnnotationBeanProcessor }
 * 支持test类和受Spring管理的bean
 * </p>
 * <p>
 * 或直接继承 {@link AbstractDataContextTest AbstractDataContextTest} 以支持方法注解、参数注解和成员变量注解
 * </p>
 *
 * @author jiaozishun
 * @see AbstractDataContextTest
 * @see DataAnnotationBeanProcessor
 * @see DataContext
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {
    /**
     * key 值 （数据name）
     *
     * @return
     */
    String value();

    /**
     * 是否为原型，true时得到的是新构造的数据实例
     *
     * @return
     */
    boolean proto() default false;

    /**
     * 数据模板，构造数据时将以 {@link #value()} 为基础，与{@link #value()}数据合并后返回
     *
     * @return
     */
    String baseOn() default "";
}
