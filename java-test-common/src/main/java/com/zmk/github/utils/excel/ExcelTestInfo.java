package com.zmk.github.utils.excel;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2021/01/20/ 15:13
 * @Description
 */
@Data
public class ExcelTestInfo {
    @ImportExcel(name = "列名1", colIndex = 0)
    private String col1;
    @ImportExcel(name = "列名2",colIndex = 1)
    private String col2;
    @ImportExcel(name = "列名3",colIndex = 2)
    private String col3;
}
