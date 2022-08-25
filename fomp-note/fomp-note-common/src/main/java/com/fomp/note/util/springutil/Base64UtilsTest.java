package com.fomp.note.util.springutil;

import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Base64加密和解密0
 */
public class Base64UtilsTest {
    Base64Utils base64Utils;
    StandardCharsets standardCharsets;
    public static void main(String[] args) {
        String str = "abc";
        String encode = new String(Base64Utils.encode(str.getBytes()));
        System.out.println("加密后：" + encode);
        try {
            String decode = new String(Base64Utils.decode(encode.getBytes()), "utf8");
            System.out.println("解密后：" + decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void test1(){
        String str = "abc";
        String encode = new String(Base64Utils.encode(str.getBytes()));
        System.out.println("加密后：" + encode);
        String decode = new String(Base64Utils.decode(encode.getBytes())
                , StandardCharsets.UTF_8);
        System.out.println("解密后：" + decode);

    }
}
