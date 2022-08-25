package com.fomp.note.mapiterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Map的遍历方式
 */
public class MapIterator {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap();
        //1、
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
        }
        //2、
        for (String key :map.keySet()) {
            String value = map.get(key);
        }
        //3、
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
        }
        //4、
        for (String value:map.values()) {
            System.out.println(value);
        }
    }
}
