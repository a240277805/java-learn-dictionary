package com.zmk.github.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @Author zmk
 * @Date: 2021/01/20/ 16:55
 * @Description
 */
@Data
@AllArgsConstructor
public class ExcelDefineInfo {
    private ImportExcel excelDefine;
    private Field field;
}
