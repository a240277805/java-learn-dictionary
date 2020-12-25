package com.zmk.github.info.es;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:54
 * @Description
 */
@Data
public class ProjectDetailCodeInfo {
    /**
     * 当前总代码行数
     */
    private Integer total_row_count;
    /**
     * 提交信息
     */
    private CommitInfo commit_info;
    /**
     * 提交次数信息
     */
    private ItemIncTotalInfo commit_push_info;

    /**
     * merge 信息
     */
    private ItemIncTotalInfo merge_info;
}
