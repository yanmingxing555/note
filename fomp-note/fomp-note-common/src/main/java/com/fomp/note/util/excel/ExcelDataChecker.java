package com.fomp.note.util.excel;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

/**
 * 〈功能〉：Excel导入导出工具类
 */
public class ExcelDataChecker {

    private static final Logger logger = Logger.getLogger(ExcelDataChecker.class.getName()); // 日志打印类

    ExcelDataChecker() {
    }
    /**
     * 读取单sheet(单页) excel文件
     * @param excelFile excel文件
     * @return List<List < HashMap < String, Object>>> 读取的数据
     */
    public  List<HashMap<String, Object>> parseSingleExcelToMap(File excelFile) {
        List<HashMap<String, Object>> data = new ArrayList<>();

        //判断文件是否存在
        if (filrTypeCheck(excelFile)) return new ArrayList<>();
        //使用try-with-resources特性自动关闭资源
        try (FileInputStream inputStream = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            // 解析sheet
            data = singleSheetCheck(workbook, (byte) 0);

            if (!EmptyChecker.isEmpty(data)) return data;

        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + excelFile.getName() + " 错误信息：" + e.getMessage());
            return new ArrayList<>();
        }
        return data;
    }

    private  boolean filrTypeCheck(File excelFile) {
        String fileName = excelFile.getName();
        // 获取Excel后缀名
        if (EmptyChecker.isEmpty(fileName) || fileName.lastIndexOf('.') < 0 || !excelFile.exists()) {
            logger.warning("指定的Excel文件不存在或者存在异常！");
            return true;
        }
        return false;
    }

    /**
     * 读取多sheet(多页) excel文件
     *
     * @param excelFile excel文件
     * @return List<List < HashMap < String, Object>>> 读取的数据
     */
    public  List<List<HashMap<String, Object>>> parseComplexExcel(File excelFile) {

        List<List<HashMap<String, Object>>> listData = new ArrayList<>();

        //获取文件名
        String fileName = excelFile.getName();
        // 获取Excel后缀名
        if (filrTypeCheck(excelFile)) return new ArrayList<>();
        //使用try-with-resources特性自动关闭资源
        try (FileInputStream inputStream = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            // 解析sheet
            for (byte sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                List<HashMap<String, Object>> data = singleSheetCheck(workbook, sheetNum);
                if (!EmptyChecker.isEmpty(data))
                    listData.add(data);
            }
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return new ArrayList<>();
        }
        return listData;

    }


    /**
     * 单个sheet对象处理
     *
     * @param workbook 文件对象
     * @param sheetNum sheet页码
     * @return
     */
    private  List<HashMap<String, Object>> singleSheetCheck(Workbook workbook, byte sheetNum) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(sheetNum);
        // 获取第一行数据
        int firstRowNum = sheet.getFirstRowNum();
        //获取到行数据
        Row firstRow = sheet.getRow(firstRowNum);
        if (EmptyChecker.isEmpty(firstRow)) {
            logger.warning("解析Excel失败，在第一行没有读取到任何数据！");
        }

        // 解析每一行的数据，构造数据对象
        int rowStart = firstRowNum + 1;
        int rowEnd = sheet.getPhysicalNumberOfRows();
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            HashMap<String, Object> rowsData = new HashMap<>();
            Row row = sheet.getRow(rowNum);
            if (null == row) {
                continue;
            }
            for (int col = 0; col < sheet.getRow(0).getPhysicalNumberOfCells(); col++) {
                Cell cell = row.getCell(col);
                String cellValue = convertCellValueToString(cell);
                rowsData.put(sheet.getRow(0).getCell(col).getStringCellValue(), cellValue);
            }
            list.add(rowsData);
        }
        return list;
    }


    /**
     * 将单元格内容转换为字符串
     *
     * @param cell
     * @return
     */
    private  String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字

                if (DateUtil.isCellDateFormatted(cell)) {
                    Date tempValue = cell.getDateCellValue();
                    returnValue = dateCheck(tempValue);
                } else {
                    // 返回数值类型的值
                    Object inputValue = null;// 单元格值
                    Long longVal = Math.round(cell.getNumericCellValue());
                    Double doubleVal = cell.getNumericCellValue();
                    if (Double.parseDouble(longVal + ".0") == doubleVal) {   //判断是否含有小数位.0
                        inputValue = longVal;
                    } else {
                        inputValue = doubleVal;
                    }
                    DecimalFormat df = new DecimalFormat("#.####");    //格式化为四位小数，按自己需求选择；
                    return String.valueOf(df.format(inputValue));
                }
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 生成Excel并写入数据信息
     *
     * @param dataList 数据列表
     * @param fileName 文件名称 如：（xx.xls或xx.xlsx）
     * @return 写入数据后的工作簿对象
     */
    public  Workbook mapExportData(List<LinkedHashMap<String, Object>> dataList, String exportFilePath, String fileName) {
        // 生成xlsx的Excel
        try {
            Workbook workbook = getWorkbook(exportFilePath, fileName);
            //取出map key作表头
            HashMap<String, Object> rowMap = dataList.get(0);
            List<String> mapKeys = new ArrayList<>(rowMap.keySet());

            // 生成Sheet表，写入第一行的列头
            Sheet sheet = buildDataSheet(workbook, mapKeys);
            //构建每行的数据内容
            int rowNum = 1;
            for (int i = 0; i < dataList.size(); i++) {
                HashMap<String, Object> data = dataList.get(i);
                if (EmptyChecker.isEmpty(dataList.get(i))) {
                    continue;
                }
                //输出行数据
                Row row = sheet.createRow(rowNum++);
                convertDataToRow(data, row);
            }
            return workbook;
        } catch (Exception e) {
            logger.warning("excel生成过程中发生异常 ,{}" + e.getMessage());
            return null;
        }
    }

    /**
     * 生成Excel并写入数据信息
     *
     * @param dataList 数据
     * @param fileName 文件名称 如：（xx.xls或xx.xlsx）
     * @param <T>      bean泛型
     * @return 写入数据后的工作簿对象
     */
    public  <T> Workbook beanExportData(List<T> dataList, String exportFilePath, String fileName) {
        if (EmptyChecker.isEmpty(dataList)) {
            throw new NullPointerException("The data list is empty");
        }
        // 生成xlsx的Excel
        try {
            Workbook workbook = getWorkbook(exportFilePath, fileName);
            // 生成Sheet表，写入第一行的列头
            Class<T> tClassOne = (Class<T>) (dataList.get(0).getClass());
            Field[] fieldsOne = tClassOne.getDeclaredFields();
            Sheet sheet = buildDataSheet(workbook, fieldsOne);
            for (int i = 0; i < dataList.size(); i++) {
                if (EmptyChecker.isEmpty(dataList.get(i))) {
                    continue;
                }
                Class<T> tClass = (Class<T>) (dataList.get(i).getClass());

                Field[] fields = tClass.getDeclaredFields();

                //输出行数据
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < fields.length; j++) {
                    //得到属性
                    Field field = fields[j];
                    String beanValue = getBeanValue(dataList.get(i), field);
                    if (EmptyChecker.isEmpty(beanValue)) {
                        continue;
                    }
                    Cell cell = row.createCell(j);
                    cell.setCellValue(beanValue);
                }
            }
            return workbook;
        } catch (Exception e) {
            logger.warning("excel生成过程中发生异常 ,{}" + e.getMessage());
            return null;
        }

    }


    /**
     * 生成sheet表，并写入第一行数据（列头）
     *
     * @param workbook 工作簿对象
     * @return 已经写入列头的Sheet
     */
    private  Sheet buildDataSheet(Workbook workbook, Field[] fields) {
        Sheet sheet = workbook.createSheet();
        // 设置列头宽度
        for (int i = 0; i < fields.length; i++) {
            sheet.setColumnWidth(i, 4000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row head = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = head.createCell(i);
            //得到属性
            Field field = fields[i];
            //打开私有访问
            field.setAccessible(true);
            //获取属性
            String name = field.getName();
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    /**
     * 生成sheet表，并写入第一行数据（列头）
     *
     * @param workbook 工作簿对象
     * @return 已经写入列头的Sheet
     */
    private  Sheet buildDataSheet(Workbook workbook, List<String> keySet) {
        Sheet sheet = workbook.createSheet();
        // 设置列头宽度
        for (int i = 0; i < keySet.size(); i++) {
            sheet.setColumnWidth(i, 4000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row head = sheet.createRow(0);
        for (int i = 0; i < keySet.size(); i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(keySet.get(i));
            cell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    /**
     * 设置第一行列头的样式
     *
     * @param workbook 工作簿对象
     * @return 单元格样式对象
     */
    private  CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置
        style.setAlignment(HorizontalAlignment.CENTER);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 将数据转换成行
     *
     * @param data 源数据
     * @param row  行对象
     * @return
     */
    private  void convertDataToRow(HashMap<String, Object> data, Row row) {
        Cell cell;
        int i = 0;
        final Set<String> keys = data.keySet();
        for (String key : keys) {
            cell = row.createCell(i);

            cell.setCellValue(EmptyChecker.isEmpty(data.get(key)) ? "" : dateCheck(data.get(key)));
            i++;
        }
    }

    /**
     * 时间处理
     *
     * @param obj
     * @return
     */
    private  String dateCheck(Object obj) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (obj instanceof Date || obj instanceof LocalDate) ? simpleFormat.format(obj) : String.valueOf(obj);
    }

    /**
     * list<HashMap>导出excel到本地磁盘
     *
     * @param dataVOList     数据
     * @param exportFilePath 磁盘路径，不包括excel名字
     * @param excelName      excel文件名 文件全名 如xx.xls   xx.xlsx
     */
    public  void exportExcelToDisks(List<LinkedHashMap<String, Object>> dataVOList, String exportFilePath, String excelName) {
        // 以文件的形式输出工作簿对象
        try (Workbook workbook = mapExportData(dataVOList, exportFilePath, excelName);
             //TODO 缓冲区在写大文件时可以调大一点，默认构造方法8192
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(exportFilePath + excelName),1024)) {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * list<HashMap>导出excel到本地磁盘
     *
     * @param data           数据
     * @param exportFilePath 磁盘路径，不包括excel名字
     * @param excelName      excel文件名 文件全名 如xx.xls   xx.xlsx
     */
    public  <T> void exportBeanExcelToDisks(List<T> data, String exportFilePath, String excelName) {
        // 以文件的形式输出工作簿对象
        try (Workbook workbook = beanExportData(data, exportFilePath, excelName);
             //TODO 缓冲区在写大文件时可以调大一点，默认构造方法8192
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(exportFilePath + excelName),1024)) {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            logger.warning("输出Excel时发生错误，错误原因：" + e.getMessage());
        }

    }

    /**
     * 利用反射读取excel
     *
     * @param file   excel文件
     * @param tClass bean
     * @param <T>    泛型
     * @return list<bean>数据
     */
    public  <T> List<T> readExcelToBean(File file, Class<T> tClass) {
        List<T> listBean = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            List<List<String>> list = readExcel(inputStream);

            //-----------------------遍历数据到实体集合开始-----------------------------------
            Field[] fields = tClass.getDeclaredFields();
            T uBean = null;
            for (int i = 1; i < list.size(); i++) {// i=1是因为第一行不要
                uBean = (T) tClass.newInstance();
                List<String> listStr = list.get(i);
                for (int j = 0; j < listStr.size(); j++) {
                    if (j >= fields.length) {
                        break;
                    }
                    Field field = fields[j];
                    String dataString = listStr.get(j);
                    field.setAccessible(true);
                    if (EmptyChecker.notEmpty(dataString)) {
                        Class<?> type = field.getType();
                        dataTypeCheck(type, field, uBean, dataString);
                    }
                }
                listBean.add(uBean);
            }
        } catch (Exception e) {
            logger.warning("excel读取过程中发生异常 ,{}" + e.getMessage());
        }
        return listBean;
    }

    /**
     * 读取excel 为listList<List<String>> 经测试速度最快，
     * 比List<HashMap>格式快3倍左右
     *
     * @param is
     * @return List<List < String>> 第一行为表头
     * @throws IOException
     */
    public  List<List<String>> readExcel(InputStream is)
            throws IOException {
        List<List<String>> dataLst = new ArrayList<>();
        try (Workbook wb = WorkbookFactory.create(is)) {
            //得到第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            //得到Excel的行数
            int totalRows = sheet.getPhysicalNumberOfRows();
            // 得到Excel的列数
            int totalCells = 0;
            if (totalRows >= 1 && sheet.getRow(0) != null) {
                totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            }

            /** 循环Excel的行 */
            for (int r = 0; r < totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null)
                    continue;
                List<String> rowLst = new ArrayList<>();
                /** 循环Excel的列 */
                for (int c = 0; c < totalCells; c++) {
                    Cell cell = row.getCell(c);
                    String cellValue = "";
                    if (null != cell) {
                        HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
                        cellValue = hSSFDataFormatter.formatCellValue(cell);
                    }
                    rowLst.add(cellValue);
                }

                dataLst.add(rowLst);
            }
        } catch (Exception e) {
            logger.warning("excel读取过程中发生异常 ,{}" + e.getMessage());
        }
        return dataLst;

    }

    /**
     * excel to bean
     *
     * @param file   excel文件
     * @param tClass bean
     * @param <T>    泛型
     * @return list<bean>
     */
    public  <T> List<T> excelToBean(File file, Class<T> tClass) {

        List<T> listBean = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file)) {

            List<HashMap<String, Object>> listData = parseSingleExcelToMap(file);

            //-----------------------遍历数据到实体集合开始-----------------------------------

            Field[] fields = tClass.getDeclaredFields();
            T uBean = null;
            for (int i = 0; i < listData.size(); i++) {
                uBean = (T) tClass.newInstance();
                HashMap<String, Object> map = listData.get(i);
                for (Map.Entry<String, Object> entry : map.entrySet()) {

                    for (int k = 0; k < fields.length; k++) {
                        //取出每行
                        Field field = fields[k];

                        field.setAccessible(true);
                        String dataStr = entry.getValue().toString();

                        Class<?> type = field.getType();
                        String name = field.getName();
                        if (entry.getKey().equals(name)) {
                            dataTypeCheck(type, field, uBean, dataStr);
                        }
                    }
                }
                listBean.add(uBean);
            }
        } catch (Exception e) {
            logger.warning("excel转换bean发生异常 ,{}" + e.getMessage());
        }
        return listBean;
    }

    /**
     * 类型处理
     *
     * @param type
     * @param field
     * @param uBean
     * @param dataStr
     * @param <T>
     */
    private  <T> void dataTypeCheck(Class<?> type, Field field, T uBean, String dataStr) {
        try {

            if (type == String.class) {
                field.set(uBean, dataStr);
            }
            if (type == Integer.class || type == int.class) {
                field.set(uBean, Integer.parseInt(dataStr));
            }
            if (type == Double.class || type == double.class) {
                field.set(uBean, Double.parseDouble(dataStr));
            }
            if (type == Float.class || type == float.class) {
                field.set(uBean, Float.parseFloat(dataStr));
            }
            if (type == Long.class || type == long.class) {
                field.set(uBean, Long.parseLong(dataStr));
            }
            if (type == Boolean.class || type == boolean.class) {
                field.set(uBean, Boolean.parseBoolean(dataStr));
            }
            if (type == Short.class || type == short.class) {
                field.set(uBean, Short.parseShort(dataStr));
            }
            if (type == Byte.class || type == byte.class) {
                field.set(uBean, Byte.parseByte(dataStr));
            }
            if (type == Character.class || type == char.class) {
                field.set(uBean, dataStr.charAt(0));
            }
            if (type == Date.class || type == LocalDate.class) {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                field.set(uBean, simpleFormat.parse(String.valueOf(dataStr)));
            }
        } catch (Exception e) {
            logger.warning("反射泛型处理发生异常 ,{}" + e.getMessage());
        }

    }

    /**
     * 拿到反射的值
     *
     * @param field T
     * @return Bean Value
     */
    private  String getBeanValue(Object obj, Field field) {
        try {
            //设置可以访问私有变量
            field.setAccessible(true);

            //获取属性的名字
            String name = field.getName();

            //将属性名字的首字母大写
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

            //整合出 getId() 属性这个方法
            Method m = obj.getClass().getMethod("get" + name);

            return dateCheck(m.invoke(obj));

        } catch (Exception e) {

            logger.warning("反射泛型处理发生异常 ,{}" + e.getMessage());

            return null;
        }

    }

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     *
     * @param fileName 文件名称 如：（xx.xls或xx.xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    private  Workbook getWorkbook(String exportFilePath, String fileName) throws IllegalAccessException {
        // 获取Excel后缀名
        if (EmptyChecker.isEmpty(fileName) || fileName.lastIndexOf('.') < 0) {
            logger.warning("指定的Excel文件不存在或者存在异常！");
            throw new IllegalStateException("指定文件类型异常");
        }
        Workbook workbook = null;
        if (fileName.contains(".xlsx")) {
            //这样表示SXSSFWorkbook只会保留100条数据在内存中，其它的数据都会写到磁盘里，这样的话占用的内存就会很少
            workbook = new SXSSFWorkbook(getXSSFWorkbook(exportFilePath + fileName), 100);
        } else if (fileName.contains(".xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalAccessException("指定excel类型异常");
        }
        return workbook;
    }

    public  XSSFWorkbook getXSSFWorkbook(String filePath) {
        XSSFWorkbook workbook = null;
        BufferedOutputStream outputStream = null;
        try {
            File fileXlsxPath = new File(filePath);
            outputStream = new BufferedOutputStream(new FileOutputStream(fileXlsxPath));
            workbook = new XSSFWorkbook();
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbook;
    }
}
