/**
 * ctfo.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.ctfo.os.cms.common.exception;

/**
 * 基础异常类
 * @title BaseException
 * @description 基础异常类. 
 *
 * @Date 2020-07-30
 * @version 1.0
 */
public abstract class BaseException extends RuntimeException {
    /**
     * uid
     */
    private static final long serialVersionUID = 8037891447646609768L;

    /**
     * 默认构造函数
     */
    public BaseException() {
    }

    /**
     * 构造函数
     * @param errMsg 异常消息
     */
    public BaseException(String errMsg) {
        super(errMsg);
    }

    /**
     * 构造函数
     * @param cause 原始异常
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     * @param errMsg 异常消息
     * @param cause 原始异常
     */
    public BaseException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

}
