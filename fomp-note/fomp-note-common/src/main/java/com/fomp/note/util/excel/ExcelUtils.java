package com.fomp.note.util.excel;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * ==================================================
 * @ClassName：ExcelUtils
 * @Description：Excel导入导出工具类
 * ==================================================
 */
public class ExcelUtils {

    private static final ExcelDataChecker excelDataChecker = new ExcelDataChecker();
    private static final Logger logger = Logger.getLogger(ExcelUtils.class.getName()); // 日志打印类

    private ExcelUtils() {

    }
    /**
     * 读取单sheet(单页) excel文件
     * @param excelFile excel文件
     * @return List<List < HashMap < String, Object>>> 读取的数据
     */
    public static List<HashMap<String, Object>> parseSingleExcelToMap(@NotNull File excelFile) {
        return excelDataChecker.parseSingleExcelToMap(excelFile);
    }

    /**
     * 读取多sheet(多页) excel文件
     * 自动的，一页则一个，多页则多个
     * @param excelFile excel文件
     * @return List<List < HashMap < String, Object>>> 读取的数据
     */
    public static List<List<HashMap<String, Object>>> parseComplexExcel(@NotNull File excelFile) {
        return excelDataChecker.parseComplexExcel(excelFile);
    }

    /**
     * 读取单sheet(单页) excel文件为bean
     * 此速度最快，要求顺序
     * *****************************************
     * * 一定注意excel列的顺序要和bean字段的顺序一致  *
     * *****************************************
     *
     * @param excelFile excel文件
     * @param tClass    bean
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcelToBean(@NotNull File excelFile, @NotNull Class<T> tClass) {
        return excelDataChecker.readExcelToBean(excelFile, tClass);
    }

    /**
     * 读取单sheet(单页) excel文件为bean
     * 此方法速度逊色一些，不要求顺序，在不能保证字段和excel列顺序的情况下使用
     *
     * @param file   excel文件
     * @param tClass 实体类
     * @param <T>    泛型
     * @return List<T>   List<Bean>
     */
    public static <T> List<T> excelToBean(@NotNull File file, @NotNull Class<T> tClass) {
        return excelDataChecker.excelToBean(file, tClass);
    }

    /**
     * 导出excel到本地磁盘
     *
     * @param dataVOList     数据
     * @param exportFilePath 磁盘路径,注意后面有\\  如：C:\\WorkFile\\
     * @param excelName      excel文件名 文件全名 如xx.xls   xx.xlsx
     */
    public static void exportExcelToDisks(@NotNull List<LinkedHashMap<String, Object>> dataVOList, @NotNull String exportFilePath, @NotNull String excelName) {

        excelDataChecker.exportExcelToDisks(dataVOList, exportFilePath, excelName);
    }
    /**
     * 导出excel到本地磁盘
     *
     * @param dataVOList     bean类型数据
     * @param exportFilePath 磁盘路径,注意后面有\\  如：C:\\WorkFile\\
     * @param excelName      excel文件名 文件全名 如xx.xls   xx.xlsx
     */
    public static <T>void exportBeanExcelToDisks(@NotNull List<T> dataVOList, @NotNull String exportFilePath, @NotNull String excelName) {
        excelDataChecker.exportBeanExcelToDisks(dataVOList, exportFilePath, excelName);
    }

    /**
     * 下载excel
     * @param response   HttpServletResponse对象
     * @param excelName  文件名 文件全名 如xx.xls   xx.xlsx
     * @param dataVOList 数据
     */
    public static void mapExportExcelToResponse(@NotNull HttpServletResponse response, @NotNull String excelName, @NotNull List<LinkedHashMap<String, Object>> dataVOList) {

        // 根据业务需求获取需要写入Excel的数据列表 dataList
        try (Workbook workbook = excelDataChecker.mapExportData(dataVOList, "",excelName);) {
            putTogetherData(response, workbook, excelName);
        } catch (Exception e) {
            logger.warning("写入Excel过程出错！错误原因：" + e.getMessage());
        }
    }

    /**
     * 下载excel
     *
     * @param response   HttpServletResponse对象
     * @param excelName  文件名 文件全名 如xx.xls   xx.xlsx
     * @param dataVOList 数据
     */
    public static <T> void beanExportExcelToResponse(@NotNull HttpServletResponse response, @NotNull String excelName, @NotNull List<T> dataVOList) {
        // 根据业务需求获取需要写入Excel的数据列表 dataList
        try (Workbook workbook = excelDataChecker.beanExportData(dataVOList,"", excelName)) {
            putTogetherData(response, workbook, excelName);
        } catch (Exception e) {
            logger.warning("写入Excel过程出错！错误原因：" + e.getMessage());
        }
    }

    /**
     * 将公共的HttpServletResponse组装提出来
     *
     * @param response  上下文对象
     * @param workbook  工作簿对象
     * @param excelName 文件名 文件全名 如xx.xls   xx.xlsx
     */
    private static void putTogetherData(HttpServletResponse response, Workbook workbook, String excelName) throws IOException {
        OutputStream out = null;
        // 写入Excel文件到前端
        if (null != workbook) {
            excelName = new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment;filename=" + excelName);
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.flushBuffer();
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        }
    }
}
