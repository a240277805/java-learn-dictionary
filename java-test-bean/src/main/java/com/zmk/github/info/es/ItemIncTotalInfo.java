package com.zmk.github.info.es;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:39
 * @Description
 */
@Data
public class ItemIncTotalInfo {
    /**
     * 每日增加数量
     */
    private Long inc_count;
    /**
     * 当前总数
     */
    private Long total_count;
}
