package com.fomp.note.util.zip;

import de.idyl.winzipaes.AesZipFileDecrypter;
import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESDecrypterBC;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;
import de.idyl.winzipaes.impl.ExtZipEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * 解压和提取压缩包中的文件
 * 依赖jar：winzipaes-1.0.1.jar
 * 下载jar地址：https://mvnrepository.com/artifact/de.idyl/winzipaes
 * Winzipaes是Java操作ZIP文件的开源项目，支持密码，但不支持中文文件名
 */
public class AesZipUtil {
    /**
     * 压缩单个文件无密码 ：压缩时单个文件压缩
     * @param srcFile      待压缩的文件
     * @param desFile      生成的目标文件
     */
    public static void zipFile(String srcFile,String desFile) throws IOException{
        AesZipFileEncrypter.zip(new File(srcFile),new File(desFile));
    }
    /**
     * 压缩单个文件并加密 ：压缩时会把原路径的文件夹一起压缩
     * @param srcFile      待压缩的文件
     * @param desFile      生成的目标文件
     * @param passWord     压缩文件加密密码
     */
    public static void zipFile(String srcFile,String desFile,String passWord) throws IOException{
        AesZipFileEncrypter.zipAndEncrypt(new File(srcFile),new File(desFile), passWord, new AESEncrypterBC());
    }

    /**
     * 批量压缩
     * @param files 待压缩文件集合
     * @param filesNameList 待压缩文件压缩之后文件名称
     * @param fileZipName 压缩包名称
     * @param password 解压密码
     */
    public  void zipFiles(ArrayList<File> files , ArrayList<String> filesNameList, File fileZipName , String password) throws IOException {
        AESEncrypter encrypter = new AESEncrypterBC();
        AesZipFileEncrypter zipEncrypter = null;
        try {
            zipEncrypter  = new AesZipFileEncrypter(fileZipName,encrypter);
            //zipEncrypter.setEncoding("utf-8");
            for(int i=0;i<files.size();i++){
                zipEncrypter.add(files.get(i),filesNameList.get(i),password);
            }
        }finally{
            if(zipEncrypter != null){
                try { zipEncrypter.close();} catch (IOException e) {}
            }
        }
    }

    /**
     * 给指定的压缩文件进行加密
     * @param srcZipFile      待加密的压缩文件
     * @param desFile         加密后的目标压缩文件
     * @param passWord        压缩文件加密密码
     */
    public static void encryptZipFile(String srcZipFile,String desFile,String passWord) throws IOException{
        AesZipFileEncrypter.zipAndEncryptAll(new File(srcZipFile), new File(desFile), passWord, new AESEncrypterBC());
    }

    /**
     * 解密抽取压缩文件中的某个文件
     * @param srcZipFile        加密的压缩文件
     * @param extractFileName   抽取压缩文件中的某个文件的名称
     * @param desFile           解压后抽取后生成的目标文件
     * @param passWord          解压密码
     */
    public static void  decrypterZipFile(String srcZipFile,String extractFileName,String desFile,String passWord)throws IOException, DataFormatException {
        AesZipFileDecrypter zipFile = new AesZipFileDecrypter(new File(srcZipFile),new AESDecrypterBC());
        ExtZipEntry entry = zipFile.getEntry(extractFileName);
        zipFile.extractEntry(entry, new File(desFile),passWord);
    }
    /**
     * 解压文件
     * @param srcZipFile    待解压的文件
     * @param descFileNames 生成的目标文件名称集合
     */
    public static void unZipFile(String srcZipFile,List<String> descFileNames,String password) throws IOException, DataFormatException {
        AesZipFileDecrypter zipFile = new AesZipFileDecrypter(new File(srcZipFile),new AESDecrypterBC());
        List<ExtZipEntry> entryList = zipFile.getEntryList();
        for (int i = 0; i < entryList.size(); i++) {
            ExtZipEntry extZipEntry = entryList.get(i);
            zipFile.extractEntry(extZipEntry,new File(descFileNames.get(i)),password);
        }
    }
    public static void main(String[] args) throws Exception {
        String srcFile="D:\\test\\source\\file4.txt";
        String desFile1="D:\\test\\aes\\file4.zip";
        String desFile2="D:\\test\\aes\\file4-pass.zip";
        String desFile3="D:\\test\\aes\\file04.txt";
        //AesZipUtil.zipFile(srcFile,desFile1);
        //AesZipUtil.zipFile(srcFile, desFile2,"123456");
        //AesZipUtil.encryptZipFile(desFile1, desFile2,"123456");
        //AesZipUtil.decrypterZipFile(desFile2,"file4.txt", "D:\\test\\aes\\file4.txt","123456");
        //AesZipUtil.unZipFile(desFile2, Arrays.asList("D:\\test\\aes\\file4.txt"),"123456");
    }
}
