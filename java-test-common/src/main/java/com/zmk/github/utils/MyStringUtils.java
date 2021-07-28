package com.zmk.github.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.zmk.github.info.kafka.events.KafkaBodyAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @Author zmk
 * @Date: 2020/12/16/ 14:44
 * @Description
 */
public class MyStringUtils {
    static Logger logger = LoggerFactory.getLogger(MyStringUtils.class);


    /**
     * String 转 任何类型 ，转换不了 则返回空
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getTValueElseNull(String text, Class<T> clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (Exception ex) {
            logger.error("String转换类型 class 失败: value: {} ,type: {}", text, clazz.toString());
            return null;
        }
    }

    /**
     * String 转 任何类型 ，转换不了 则返回空
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getTArrayValueElseNull(String text, Class<T> clazz) {
        try {
            return JSONObject.parseArray(text, clazz);
        } catch (Exception ex) {
            logger.error("String转换类型 class 失败: value: {} ,type: {}", text, clazz.toString());
            return null;
        }
    }

    /**
     * 截取字符串
     *
     * @param originStr
     * @param stackLength
     * @return
     */
    public static String getCutStr(String originStr, Integer stackLength) {
        if (Objects.isNull(stackLength)) {
            stackLength = 500;
        }
        return originStr.substring(0, originStr.length() > stackLength ? stackLength : originStr.length());
    }
}
