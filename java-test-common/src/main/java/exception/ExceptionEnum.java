package com.ctfo.os.cms.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * -99 开头的异常发邮件提醒
 * -981 开头跳登录页
 * 不分正负 第四位 为8 的 警告弹框，其他 为红色弹框
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    //应答结果状态码——成功
//    ERROR_CODE_SUCCESS(0, "success"),
    //    应答结果状态码——通用错误
    ERROR_CODE_ERROR(-990, "custom error"),
    //应答结果状态码——通用错误--系统异常
    ERROR_CODE_BUSINESS(-998, "business error"),
    ERROR_CODE_PARAMETER(-997, "parameter error"),
    ERROR_CODE_OTHER(-996, "system error"),
    //应答结果状态码——通用错误--权限错误
    ERROR_CODE_AUTH(-995, "auth error"),
    //应答结果状态码——通用错误--权限错误
    MAIL_SEND_ERROR(-994, "mail send error"),
    //应答结果状态码——通用错误--会话失效
    ERROR_CODE_SESSION_FAILURE(-993, "session failure error"),
    //跳登录页异常
    REDIRECT_HOME_ERROR(-981, "session failure error"),


//    GITLAB_QUERY_ERROR(-920001, "gitlab请求异常!"),
//    GITLAB_USER_TOKEN_ERROR(-920002, "获取gitlab账号异常，请确保gitlab账号已开通!"),
//    GITLAB_PROJECT_CREATE_ERROR(-999003, "gitlab创建代码库异常"),
//
//    //    以下为业务提示信息异常，为非异常；32xxxx开头的异常需前端处理成Warning
//    CHECK_RELATION_DUPLICATE_INSERT(-320000, "重复添加人员"),
//    CHECK_TEST_PLAN_DUPLICATE_NAME(-320001, "测试计划名称重复，请修改后重新添加"),
//    CREATE_TEST_PLAN_LIMIT(-320002, "父级测试计划不是顶层测试计划，不可在其下创建测试计划"),
//    PROJECT_ID_NULL(-320003, "项目id不能为空"),
//    CHECK_TAG_REPEAT(-320004, "标签名称重复"),
//    LOGIN_ERROR(0320005, "账号密码异常"),

    ERROR_CODE_SYSTEM(-999, "system error");

    private Integer code;
    private String msg;
}
