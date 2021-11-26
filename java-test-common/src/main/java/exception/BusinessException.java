package com.ctfo.os.cms.common.exception;

import com.ctfo.os.cms.common.enums.exceptions.BusinessExceptionEnum;
import lombok.Getter;

/**
 * 业务异常.
 *
 * @version 1.0
 * @title BusinessException
 * @description 业务异常.
 * @Date 2020-07-30
 */
@Getter
public class BusinessException extends BaseException {
	private Integer code = -100000;
	/**
	 * uid
	 */
	private static final long serialVersionUID = 7805981291350841911L;

	/**
	 * 默认构造函数
	 */
	public BusinessException() {
		super("Business exception.");
	}

	/**
	 * 构造函数
	 *
	 * @param errMsg 异常消息
	 */
	public BusinessException(String errMsg) {
		super(errMsg);
	}

	public BusinessException(BusinessExceptionEnum exceptionEnum) {
		super(exceptionEnum.getMsg());
		this.code = exceptionEnum.getCode();
	}

	public BusinessException(BusinessExceptionEnum exceptionEnum, String msg) {
		super(msg);
		this.code = exceptionEnum.getCode();
	}

	/**
	 * 发自定义返回值异常专用
	 *
	 * @param code
	 * @param msg
	 */
	public BusinessException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	/**
	 * 数据库操作条数异常
	 *
	 * @return
	 */
	public static BusinessException DBCountError() {
		return new BusinessException(BusinessExceptionEnum.DB_COUNT_ERROR);
	}

	/**
	 * 发邮件异常
	 *
	 * @param msg
	 * @return
	 */
	public static BusinessException MailException(String msg) {
		return new BusinessException(BusinessExceptionEnum.CUSTOM_MAIL_DIALOG_ERROR.getCode(), msg);
	}

	/**
	 * 发邮件(无弹框)异常
	 *
	 * @param msg
	 * @return
	 */
	public static BusinessException MailNoDailogException(String msg) {
		return new BusinessException(BusinessExceptionEnum.NO_DIALOG_ERROR.getCode(), msg);
	}

	/**
	 * 发邮件异常,(不带弹框)
	 *
	 * @return
	 */
	public static BusinessException MailException() {
		return new BusinessException(BusinessExceptionEnum.NO_DIALOG_ERROR);
	}

	/**
	 * 返回枚举类型异常
	 *
	 * @param exceptionEnum
	 * @return
	 */
	public static BusinessException EnumExcetion(BusinessExceptionEnum exceptionEnum) {
		return new BusinessException(exceptionEnum);
	}

	/**
	 * 发送不发邮件，带弹框业务异常
	 *
	 * @param msg
	 * @return
	 */
	public static BusinessException DialogException(String msg) {
		return new BusinessException(BusinessExceptionEnum.CUSTOM_NO_MAIL_DIALOG_ERROR.getCode(), msg);
	}


	/**
	 * 业务异常
	 *
	 * @param cause 原始异常
	 */
	public BusinessException(Throwable cause) {
		super(cause);
	}
}
