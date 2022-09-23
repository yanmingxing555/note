package com.fomp.note.util.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;
import java.util.List;

/**
 * Zip4j工具类：压缩zip与解压zip ：有无密码均支持
 * 依赖第三方jar包：zip4j-1.3.2.jar
 * jar下载地址：https://mvnrepository.com/artifact/net.lingala.zip4j/zip4j/1.3.2
 * 支持：jdk1.6
 */
public class Zip4jUtils {

    /**
     * 压缩解压密码
     */
    private static String PASSWORD = "";
    /**
     * 压缩和解压的文件名称编码
     */
    private static String FILENAME_CHARSET = "GBK";

    /**
     * 把文件fromFile压缩为toFile
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)
     */
    public static void zip(String fromFile, String toFile) throws ZipException{
        zip(fromFile, toFile, true);
    }

    /**
     * 把文件fromFile压缩为toFile
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)
     * @param password -> 设置解密密码，注：该密码最对文件控制
     */
    public static void zip(String fromFile, String toFile, String password) throws ZipException{
        PASSWORD = password;
        zip(fromFile, toFile, true);
    }

    /**
     * 把文件fromFile压缩为toFile
     * 注：如果不覆盖，则会继续将新文件写入原有的zip包中
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)
     * @param cover -> 是否覆盖原有文件，true=覆盖
     */
    public static void zip(String fromFile, String toFile, boolean cover) throws ZipException{
        File zipFile = new File(toFile);
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        if (zipFile.exists() && cover) {
            zipFile.delete(); //覆盖原有文件
        }
        doZip(new File(fromFile), toFile);
    }

    /**
     * 把文件fromFile压缩为toFile
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)
     */
    private static void doZip(File fromFile, String toFile) throws ZipException{
        ZipFile zip=new ZipFile(toFile);
        zip.setFileNameCharset(FILENAME_CHARSET);
        ZipParameters param = new ZipParameters();
        //设置压缩方式（默认方式）
        param.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        //设置压缩级别
        param.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        setupPassword(param);
        if (fromFile.isFile()){
            zip.addFile(fromFile, param);
        }else{
            zip.addFolder(fromFile, param);
        }
    }
    /**
     * 压缩ZIP文件
     * @param fromFile -> 需要压缩的ZIP文件地址
     */
    public static void unZip(String fromFile) throws ZipException{
        File file = new File(fromFile);
        doUnZip(file, file.getParent());
    }
    
    /**
     * 压缩ZIP文件
     * @param fromFile -> 需要压缩的ZIP文件地址
     * @param toPath -> 压缩后的文件存放地址
     */
    public static void unZip(String fromFile, String toPath) throws ZipException{
        if (toPath == null || "".equals(toPath.trim())){
            unZip(fromFile);
        }else{
            doUnZip(new File(fromFile), toPath);
        }
    }

    /**
     * 压缩ZIP文件
     * @param fromFile -> 需要压缩的ZIP文件地址
     * @param toPath -> 压缩后的文件存放地址
     * @param Password -> 解压密码
     */
    public static void unZip(String fromFile, String toPath, String Password) throws ZipException{
        PASSWORD = Password;
        unZip(fromFile, toPath);
    }
    
    /**
     * 解压ZIP文件
     * @param fromFile -> 需要压缩 的zip文件
     * @param toPath -> 压缩后存放的目录
     */
    private static void doUnZip(File fromFile, String toPath) throws ZipException {
        ZipFile zipFile = new ZipFile(fromFile);
        zipFile.setFileNameCharset(FILENAME_CHARSET);
        if (!zipFile.isValidZipFile()) {
            throw new ZipException("文件不合法或不存在！");
        }
        checkEncrypted(zipFile);
        List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = fileHeaderList.get(i);
            zipFile.extractFile(fileHeader, toPath);
        }
    }

    /**
     * 检查压缩包是否需要密码：解压时使用
     */
    private static void checkEncrypted(ZipFile zip) throws ZipException {
        if (zip.isEncrypted()) {
            zip.setPassword(PASSWORD);
        }
    }

    /**
     * 设置解压密码：压缩时使用
     * @param param -> ZipParameters 参数对象
     */
    private static void setupPassword(ZipParameters param) {
        if (PASSWORD != null && PASSWORD.trim().length()>0){
            // 设置加密文件
            param.setEncryptFiles(true);
            // 设置加密方式（必须要有加密算法）
            param.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            // 设置秘钥长度
            param.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);  
            param.setPassword(PASSWORD);
        }
    }
    private Zip4jUtils(){}

    public static void main(String[] args) throws ZipException {
        String zipFromFile = "D:\\test\\source\\file1.txt";
        String zipToFile = "D:\\test\\zip4j\\file1-pass.zip";
        //压缩文件夹或文件到指定目录下
        //Zip4jUtils.zip(zipFromFile, zipToFile);
        //Zip4jUtils.zip(zipFromFile, zipToFile, true);
        //Zip4jUtils.zip(zipFromFile, zipToFile, "123456");

        String unZipFromFile1 = "D:\\test\\zip4j\\file1.zip";
        String unZipFromFile2 = "D:\\test\\zip4j\\file1-pass.zip";
        String unZipToPath = "D:\\test\\zip4j\\toPath";
        //解压文件到指定目录
        //Zip4jUtils.unZip(unZipFromFile1);
        //Zip4jUtils.unZip(unZipFromFile1, unZipToPath);
        //Zip4jUtils.unZip(unZipFromFile2, unZipToPath, "123456");
    }
}