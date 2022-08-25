package com.fomp.note.util.springutil;

import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * 数据进行序列化和反序列化处理。
 * 传统的做法是某个类实现Serializable接口，然后重新它的writeObject和readObject方法。
 * 使用org.springframework.util包下的SerializationUtils工具类，能更轻松实现序列化和反序列化功能。
 */
public class SerializationUtilsTest {
    SerializationUtils serializationUtils;
    public static void main(String[] args) {
        Map<String, String> map = Maps.newHashMap();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        byte[] serialize = SerializationUtils.serialize(map);
        Object deserialize = SerializationUtils.deserialize(serialize);
        System.out.println(deserialize);

    }
}
