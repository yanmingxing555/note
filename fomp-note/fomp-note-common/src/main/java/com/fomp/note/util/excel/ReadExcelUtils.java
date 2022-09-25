package com.fomp.note.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析excel表格
 */
@Slf4j
public class ReadExcelUtils {

    /**
     * 工作簿对象
     */
    private Workbook wb;
    /**
     * 工作表
     */
    private Sheet sheet;
    /**
     * 工作行
     */
    private Row row;

    public ReadExcelUtils() {}

    /**
     * 读取文件，判断格式
     */
    public ReadExcelUtils(String filepath) throws Exception{
        // 文件路径为空时自动跳过
        if (filepath == null) {
            return;
        }
        // 获取文件后缀名
        String ext = filepath.substring(filepath.lastIndexOf("."));
        InputStream is = new FileInputStream(filepath);
        if (".xls".equals(ext)) {
            wb = new HSSFWorkbook(is);
        } else if (".xlsx".equals(ext)) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new Exception("仅支持文件后缀.xls与.xlsx格式！");
        }
    }
    /**
     * 解析入口
     */
    public Map<Integer, Map<Integer, Object>> readExcelContent(String filepath) throws Exception{
        Map<Integer, Map<Integer, Object>> map = null;
        ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
        map = excelReader.readExcelContent();
        return map;
    }
    /**
     * 读取表头
     */
    public String[] readExcelTitle() throws Exception {
        if (wb == null) {
            throw new Exception("工作簿Workbook对象为空！");
        }
        // 读取第一张（索引为零）表格
        sheet = wb.getSheetAt(0);
        // 获取第一张表格的第一行
        row = sheet.getRow(0);
        // 获取标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("标题总列数colNum:" + colNum);
        // 以列数作为数组元素个数创建数组
        String[] title = new String[colNum];
        // 将表头字段装进数组
        for (int i = 0; i < colNum; i++) {
            title[i] = row.getCell(i).getCellFormula();
        }
        return title;
    }
    /**
     * 读取 Excel 数据内容，返回包含单元格数据内容的Map对象
     */
    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception("工作簿Workbook对象为空！");
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
        // 获取工作簿的第一张表
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        // 获取第一行
        row = sheet.getRow(0);
        // 获取列数
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            // 第二行开始
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            // 读取该行的每列数据，并存入map集合中
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            // 将每行数据装入map数组中
            content.put(i, cellValue);
        }
        return content;
    }

    /**
     * 根据Cell类型设置数据
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前单元格的类型
            switch (cell.getCellType()) {
                case NUMERIC: {
                    // 如果当前Cell的Type为NUMERIC
                    cellvalue = (int) cell.getNumericCellValue();
                    break;
                }
                case FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {
                        // 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:
                    // 如果当前Cell的Type为字符串
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


}

