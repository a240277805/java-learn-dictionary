package com.zmk.github.info.es;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:48
 * @Description
 */
@Data
public class CommitInfo {
    /**
     * 每天增加的行数
     */
    private Integer inc_row_count;
    /**
     * 每天删除的行数
     */
    private Integer delete_row_count;
    /**
     * 每天增加的减去删除的行数
     */
    private Integer daily_change_row_count;
}
