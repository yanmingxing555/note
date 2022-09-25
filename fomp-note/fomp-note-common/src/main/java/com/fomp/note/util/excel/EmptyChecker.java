package com.fomp.note.util.excel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 空判断工具类
 */
public class EmptyChecker {
    private EmptyChecker() {
    }
    /**
     * 判空
     * @param obj 任意类型数据
     * @return boolean   true or false
     * @explain 空 true ，非空 false
     */
    public static boolean isEmpty(Object obj) {

        if (obj == null || "null".equals(obj.toString()) || "".equals(obj.toString())) {

            return true;
        }

        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    /**
     * 非空判断
     *
     * @param obj 任意类型数据
     * @return boolean  true or false
     * @explain 非空 true or  空 false
     */
    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 数组判空
     *
     * @param array 源数组
     * @return boolean   true or  false
     * @explain 空 true or  非空 false
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }
    /**
     * 多个数据判空
     *
     * @param obj
     * @return boolean   true or  false
     * @explain 如果任意一个参数为空，则返回true
     */
    public static boolean isAnyOneEmpty(Object... obj) {
        for (int i = 0; i < obj.length; i++) {
            boolean empty = isEmpty(obj[i]);
            if (empty) {
                return true;
            }

        }
        return false;
    }


    /**
     * 多个数据判空
     *
     * @param obj 任意数量数据
     * @return boolean   true or  false
     * @explain 如果所有为空，返回true
     */
    public static boolean isAllEmpty(Object... obj) {
        for (int i = 0; i < obj.length; i++) {
            boolean temp = notEmpty(obj[i]);

            if (temp) {
                return false;
            }

        }

        return true;
    }

    /**
     * 判断对象为空或者对象其中一个元素为空
     *
     * @param t   源数据
     * @param <T> 泛型
     * @return boolean   true or  false
     * @explain 通过反射判断对象为空或者其中一个元素为空元素为空返回true
     */
    public static <T> boolean beanIsEmpty(T t) {
        if (notEmpty(t)) {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field obj : fields) {
                if (isEmpty(getBeanValue(t, obj))) {
                    return true;
                }
            }
            return false;

        }
        return true;
    }

    /**
     * 判断对象为空或者对象所有元素为空
     *
     * @param t   源数据
     * @param <T> 泛型
     * @return boolean   true or  false
     * @explain 通过反射判断对象为空或者所有元素为空返回true
     */
    public static <T> boolean beanIsAllEmpty(T t) {
        if (notEmpty(t)) {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field obj : fields) {
                if (notEmpty(getBeanValue(t, obj))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 通过反射拿到值
     *
     * @param obj   数据源
     * @param field
     * @return
     */
    private static String getBeanValue(Object obj, Field field) {
        try {
            //设置可以访问私有变量
            field.setAccessible(true);
            //获取属性的名称
            String name = field.getName();
            //将属性名称首字母大写
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

            Method method = obj.getClass().getMethod("get" + name);
            return dateCheck(method.invoke(obj));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间类型处理
     *
     * @param obj 源数据
     * @return
     */
    private static String dateCheck(Object obj) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (obj instanceof Date || obj instanceof LocalDate) ? simpleDateFormat.format(obj) : String.valueOf(obj);
    }
}
