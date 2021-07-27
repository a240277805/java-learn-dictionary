package com.zmk.github.utils.excel;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @Author zmk
 * @Date: 2021/01/20/ 14:34
 * @Description
 */

public class ExcelUtils {
    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param input     输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcelNewV2(String sheetName, Class<T> clazz, Field input) throws Exception {
        /**
         * 1. 检查 execl 定义
         * 2. 生成 excel 定义map
         * 3. 检查表头
         * 4. 遍历各行数据
         * 4.1 检查每行数据
         * 必填 校验， 格式校验
         *
         */
        List<T> result = new ArrayList<T>();

        Workbook workbook = WorkbookFactory.create(new FileInputStream("d:/excel1.xlsx"));
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = workbook.getSheet(sheetName);
        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = workbook.getSheetAt(0);
        }
        if (sheet == null) {
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        if (rows <= 0) {
            return new ArrayList<>();
        }
        //定义列表
        HashMap<Integer, ExcelDefineInfo> excelDefineMap = getExcelDefineMap(clazz);
        //检查表头
        _checkExcelHeader(sheet, excelDefineMap);
        // 从第2行开始取数据,默认第一行是表头.
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            //转化每行数据
            T entity = _cell2Field(row, clazz, excelDefineMap);
            result.add(entity);

        }
        return result;
    }

    private static <T> T _cell2Field(Row row, Class<T> clazz, HashMap<Integer, ExcelDefineInfo> excelDefineMap) throws Exception {
//            则新建实例
        T entity = clazz.newInstance();
        for (int index : excelDefineMap.keySet()) {
            //单元格数据
            Cell cell = row.getCell(index);

            ExcelDefineInfo fieldDefineInfo = excelDefineMap.get(index);
            //excel 定义
            ImportExcel fieldDefine = fieldDefineInfo.getExcelDefine();
            //excel 对象 field
            Field field = fieldDefineInfo.getField();
            //TODO zmk 2021/1/20 检查单元格
            /**
             * 检查是否有预定义类型，
             * 预定义类型检查，转换
             * 没有则看是否能转为泛型属性
             */

            CellType cellType = cell.getCellType();
            switch (cellType) {
                case NUMERIC: // 数字
                    short format = cell.getCellStyle().getDataFormat();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = null;
                        if (format == 20 || format == 32) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                            double value = cell.getNumericCellValue();
                            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                            field.set(entity, date);
                        } else {// 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        }
                        try {
                            Date cellDateCellValue = cell.getDateCellValue();
                            field.set(entity, cellDateCellValue);
//                                    cellValue = sdf.format(cell.getDateCellValue());// 日期
                        } catch (Exception e) {
                            try {
                                throw new Exception("exception on get date data !".concat(e.toString()));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        } finally {
                            sdf = null;
                        }
                    } else {
                        BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                        field.set(entity, bd.toPlainString());// 数值 这种用BigDecimal包装再获取plainString，可以防止获取到科学计数值
                    }
                    break;
                case STRING: // 字符串
                    field.set(entity, cell.getStringCellValue());
                    break;
                case BOOLEAN: // Boolean
                    field.set(entity, cell.getBooleanCellValue());
                    break;
                case FORMULA: // 公式
                    field.set(entity, cell.getCellFormula());
                    break;
                case BLANK: // 空值
                    field.set(entity, null);
                    break;
                case ERROR: // 故障
                    field.set(entity, "ERROR VALUE");
                    break;
                default:
                    field.set(entity, "UNKNOW VALUE");
                    break;
            }
        }
        return entity;
    }

    private static HashMap<Integer, ExcelDefineInfo> getExcelDefineMap(Class clazz) {
        // 有数据时才处理 得到类的所有field.
        Field[] allFields = clazz.getDeclaredFields();
        // 定义一个map用于存放列的序号和field.
        HashMap<Integer, ExcelDefineInfo> result = new HashMap<>();
        for (int col = 0; col < allFields.length; col++) {
            Field field = allFields[col];
            // 将有注解的field存放到map中.
            if (field.isAnnotationPresent(ImportExcel.class)) {
                // 设置类的私有字段属性可访问.
                field.setAccessible(true);
                ImportExcel excelDefine = field.getAnnotation(ImportExcel.class);
                if (result.containsKey(excelDefine.colIndex())) {
                    throw new RuntimeException("模板对象定义有问题: 列index 有重复!");
                }
                result.put(excelDefine.colIndex(), new ExcelDefineInfo(excelDefine, field));
            }
        }
        return result;
    }

    private static void _checkExcelHeader(Sheet sheet, HashMap<Integer, ExcelDefineInfo> fieldMap) {
        for (Integer index : fieldMap.keySet()) {
            ExcelDefineInfo excelDefineInfo = fieldMap.get(index);
            ImportExcel excelDefine = excelDefineInfo.getExcelDefine();
            if (!sheet.getRow(0).getCell(index).getStringCellValue().equals(excelDefine.name())) {
                throw new RuntimeException("excel模板表头定义有问题: 列index名称 和定义不一致! {}");
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        try {
            List<ExcelTestInfo> result = ExcelUtils.importExcelNewV2("sheet1", ExcelTestInfo.class, null);
            System.out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
