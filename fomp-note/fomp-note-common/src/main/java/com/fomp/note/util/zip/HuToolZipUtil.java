package com.fomp.note.util.zip;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;

import java.io.File;

/**
 * Hutool工具包压缩和解压：不支持密码
 */
public class HuToolZipUtil {
    //Hutool默认使用UTF-8编码，自定义为其他编码即可（一般为GBK）。
    //将test.zip解压到e:\\aaa目录下，返回解压到的目录
    //File unzip = ZipUtil.unzip("E:\\aaa\\test.zip", "e:\\aaa", CharsetUtil.CHARSET_GBK);
    public static void zip() {
        /*****************压缩使用规范*******************/
        //1.打包到当前目录（可以打包文件，也可以打包文件夹，根据路径自动判断）
        //将aaa目录下的所有文件目录打包到d:/aaa.zip
        ZipUtil.zip("d:/aaa");

        //2.指定打包后保存的目的地，自动判断目标是文件还是文件夹
        //将aaa目录下的所有文件目录打包到d:/bbb/目录下的aaa.zip文件中
        // 此处第二个参数必须为文件，不能为目录
        ZipUtil.zip("d:/aaa", "d:/bbb/aaa.zip");

        //将aaa目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
        ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip");

        //3.可选是否包含被打包的目录。比如我们打包一个照片的目录，打开这个压缩包有可能是带目录的，
        //也有可能是打开压缩包直接看到的是文件。zip方法增加一个boolean参数可选这两种模式，以应对众多需求。

        //将aaa目录以及其目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
        ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip", true);

        //4.多文件或目录压缩。可以选择多个文件或目录一起打成zip包。
        ZipUtil.zip(FileUtil.file("d:/bbb/ccc.zip"), false,
                FileUtil.file("d:/test1/file1.txt"),
                FileUtil.file("d:/test1/file2.txt"),
                FileUtil.file("d:/test2/file1.txt"),
                FileUtil.file("d:/test2/file2.txt")
        );
    }

    public static void unzip() {
        /*****************解压使用规范*******************/
        //1.ZipUtil.unzip 解压。同样提供几个重载，满足不同需求。
        //将test.zip解压到e:\\aaa目录下，返回解压到的目录
        File unzip = ZipUtil.unzip("E:\\aaa\\test.zip", "e:\\aaa");
    }

}
