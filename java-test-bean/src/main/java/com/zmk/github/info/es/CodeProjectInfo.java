package com.zmk.github.info.es;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:36
 * @Description
 */
@Data
public class CodeProjectInfo {
    /**
     * 成员信息
     */
    private ItemIncTotalInfo member_info;
    /**
     * pull request 信息
     */
    private ItemIncTotalInfo pr_info;
    /**
     * 存储量信息
     */
    private ItemIncTotalInfo storage_info;
    /**
     * fork 信息
     */
    private ItemIncTotalInfo fork_info;
}
