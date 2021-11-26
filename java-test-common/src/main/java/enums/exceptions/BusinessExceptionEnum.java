package com.ctfo.os.cms.common.enums.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 6位 code 码
 * 前两位位 -99 发邮件
 * -981 开头前端跳到登录页
 *  -981 开头跳登录页
 *  第四位推荐设置成9
 *  不分正负 第四位 为8 的 警告弹框
 * 不分正负 第四位为0的为不弹框，其他 为红色弹框
 */
@AllArgsConstructor
@Getter
public enum BusinessExceptionEnum {

	DB_COUNT_ERROR(-9929001, "数据库操作条数异常!"),
	//通用发邮件异常（需要重写message） ,并且前段无弹框
	NO_DIALOG_ERROR(-999, "未知异常"),
	//自定义返回值异常（需要重写message），前端有弹框
	CUSTOM_MAIL_DIALOG_ERROR(-998000, "未知异常"),
	//自定义返回值异常(需要重写message)，前段有弹框，不发邮件
	CUSTOM_NO_MAIL_DIALOG_ERROR(-100902, "未知异常"),

	//981开头 登录相关  报错跳到登录页
	LOGGIN_TOKNE_EXPEIRE(-981901, "登录信息无效，请重新登录(TOKEN)!"),
	NO_USER(-981903, "用户不存在!"),
	NO_LOGGIN_TOKEN(-981902, "token信息不能为空！"),

	//以下业务异常





	;

	private Integer code;
	private String msg;
}
