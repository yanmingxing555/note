package com.fomp.note.util.zip;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩和解压文件：不支持密码
 */
public final class ZipUtils {
    public static void main(String[] args) {
        // String filePath1 = "D:\\test\\source";
        // String filePath2 = "D:\\test\\zip\\file.zip";
        // String filePath3 = "D:\\test\\zip\\zip";
        // zip(filePath1);
        // unzip(filePath2,filePath3);
    }
    /**
     * 压缩文件：默认输出到原路径下，压缩文件名=源文件名.zip
     * @param sourceFilePath 待压缩的文件路径
     */
    public static File zip(String sourceFilePath) {
        return zip(sourceFilePath,null);
    }
    /**
     * 压缩文件：若未指定压缩文件输出路径，默认输出到原路径下，压缩文件名=源文件名.zip
     * @param sourceFilePath 待压缩的文件路径
     * @param targetFilePathName 目标压缩文件路径和名称
     * @return 压缩后的文件
     */
    public static File zip(String sourceFilePath,String targetFilePathName) {
        File target = null;
        File source = new File(sourceFilePath);
        if (StringUtils.isNotBlank(targetFilePathName)){
            target = new File(targetFilePathName);
        }
        if (source.exists()) {
            //若未指定压缩文件输出路径，默认输出到原路径下
            if (target==null){
                //压缩文件名=源文件名.zip
                String zipName = source.getName() + ".zip";
                target = new File(source.getParent(), zipName);
            }
            if (target.exists()) {
                target.delete(); //删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                //添加对应的文件Entry
                addEntry("/", source, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(zos, fos);
            }
        }
        return target;
    }
 
    /**
     * 扫描添加文件Entry
     * @param base 基路径
     * @param source 源文件
     * @param zos Zip文件输出流
     */
    private static void addEntry(String base, File source, ZipOutputStream zos) throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            for (File file : source.listFiles()) {
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(entry + "/", file, zos);
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } finally {
                IOUtils.closeQuietly(bis, fis);
            }
        }
    }

    /**
     * 解压文件：默认解压到当前压缩文件父目录下
     * @param zipFilePath 压缩文件路径
     */
    public static void unzip(String zipFilePath) {
        unzip(zipFilePath,null);
    }
    /**
     * 解压文件：没有指定目标路径，默认解压到当前压缩文件父目录下
     * @param zipFilePath 压缩文件路径
     * @param targetFilePath 解压后文件路径
     */
    public static void unzip(String zipFilePath,String targetFilePath) {
        File source = new File(zipFilePath);
        if (!source.exists()) return;
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        try {
            zis = new ZipInputStream(new FileInputStream(source));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
                File target = null;
                if (StringUtils.isNotBlank(targetFilePath)){
                    target = new File(targetFilePath, entry.getName());
                }
                if (target==null){//没有指定目标路径，默认解压到当前压缩文件父目录下
                    target = new File(source.getParent(), entry.getName());
                }
                if (!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();//创建文件父目录
                }
                //写入文件
                bos = new BufferedOutputStream(new FileOutputStream(target));
                int read = 0;
                byte[] buffer = new byte[1024 * 10];
                while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bos.flush();
            }
            zis.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(zis, bos);
        }
    }
    private ZipUtils() {
    }
}