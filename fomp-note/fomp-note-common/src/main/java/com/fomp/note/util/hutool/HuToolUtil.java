package com.fomp.note.util.hutool;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;

import java.util.Date;
import java.util.List;

/**
 * @author: ymx
 * @date: 2022/9/1
 * @description: Hutool工具类的使用
 * Hutool 5.x支持JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。
 * 如果你的项目使用JDK7，请使用Hutool 4.x版本（不再更新）
 */
public class HuToolUtil {
    public static void main(String[] args) {
        testConvert();
    }

    public static void testConvert(){
        int a = 1;
        String str = Convert.toStr(a);
        System.out.println(str);
        Date date = Convert.toDate("20201223");
        System.out.println(Convert.toStr(date));
        String ss = "1,2,3,4,5,6";
        List<String> list = Convert.toList(String.class,ss.split(","));
        System.out.println(Convert.toStr(list));
        Console.log(Convert.toStr(list));
    }
}
