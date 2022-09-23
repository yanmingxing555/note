package com.fomp.note.util.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json转换工具类
 * @author YSS-YMX
 */
public class FastJsonUtil {
    /**
     * 功能描述：把JSON数据转换成指定的java对象
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) throws Exception{
    	T object = null;
    	try {
			object = JSON.parseObject(jsonData, clazz);
		} catch (RuntimeException e) {
			throw new Exception("json字符串转java对象出错");
		}
        return object;
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     * @param object java对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object object) throws Exception{
    	String str = "";
    	try {
			str = JSON.toJSONString(object);
		} catch (RuntimeException e) {
			throw new Exception("java对象转json字符串出错");
		}
        return str;
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) throws Exception{
        List<T> list = new ArrayList<T>();
        try {
			list = JSON.parseArray(jsonData, clazz);
		} catch (RuntimeException e) {
			throw new Exception("json字符串转指定javabean的list出错");
		}
    	return list;
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
     * @param jsonData JSON数据
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonData) throws Exception{
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	try {
			list = JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>(){});
		} catch (RuntimeException e) {
			throw new Exception("json字符串转指定map的list出错");
		}
    	return list;
    }

    /**
     * List<T> 转 json
     */
    public static <T> String listToJson(List<T> ts) throws Exception{
    	String jsons = "";
        try {
			jsons = JSON.toJSONString(ts);
		} catch (RuntimeException e) {
			throw new Exception("list转json字符串出错");
		}
        return jsons;
    }

    /**
     * 两个类之间值的转换
     * 从object》》tClass
     * @param object 有数据的目标类
     * @param tClass 转换成的类
     * @param <T>
     * @return 返回tClass类
     */
    public static <T> T getObjectToClass(Object object, Class<T> tClass) throws Exception{
    	T obj = null;
    	String json = "";
    	try {
			json = getBeanToJson(object);
			obj= getJsonToBean(json, tClass);
		} catch (Exception e) {
			throw new Exception(object.getClass().getName()+"转换"+tClass.getName()+"出错");
		}
        return obj;
    }
    /**
     * Object转换为JSONObject
     */
    public static JSONObject getJSONObject(Object obj) throws Exception{
    	JSONObject params = null;
    	try {
			params = JSONObject.parseObject(FastJsonUtil.getBeanToJson(obj));
		} catch (Exception e) {
			throw new Exception("Object转换为JSONObject出错"+e.getMessage());
		}
		return params;
	}

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) throws Exception{
        @SuppressWarnings("unchecked")
        List<T> ts = new ArrayList<T>();
        try {
			if (jsonString.contains("[")) {
				ts = JSONArray.parseArray(jsonString, clazz);
			}else{
				T obj = getJsonToBean(jsonString,clazz);
				ts.add(obj);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
        return ts;
    }
    /**
	 * 判断是否是Null字符串或仅包含空格的字符串
	 */
	public static boolean isNullStr(String str) {
		if (str == null 
			|| str.trim().length() == 0
			|| str.trim().equals("null")) {
			return true;
		}
		return false;
	}
}