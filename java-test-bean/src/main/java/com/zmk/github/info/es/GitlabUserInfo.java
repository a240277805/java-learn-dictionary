package com.zmk.github.info.es;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:45
 * @Description
 */
@Data
public class GitlabUserInfo {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户邮箱
     */
    private String email;
}
