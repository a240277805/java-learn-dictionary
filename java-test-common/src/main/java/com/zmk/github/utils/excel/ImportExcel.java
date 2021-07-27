package com.zmk.github.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 *
 * @author zmk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImportExcel {

    /**
     * 导出到Excel中的名字.
     */
    public String name();

    /**
     * 列位置
     *
     * @return
     */
    public int colIndex();

    public boolean canEmpty() default true;

    /**
     * 日期格式, 如: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     */
    public String readConverterExp() default "";


    /**
     * 当值为空时,字段的默认值
     */
    public String defaultValue() default "";


    public String errorMsg() default "属性转换错误!";
}

