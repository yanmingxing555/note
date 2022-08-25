package com.fomp.note.util.springutil;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 对数据进行加密处理，比如：md5或sha256，可以使用apache的org.apache.commons.codec.digest包下的DigestUtils类
 */
public class DigestUtilsTest {
    public static void main(String[] args) {
        // md5加密
        String md5Hex = DigestUtils.md5Hex("shawn222");
        System.out.println(md5Hex);

        // sha256加密
        String sha256Hex = DigestUtils.sha256Hex("shawn222");
        System.out.println(sha256Hex);
    }
}
