package com.fomp.note.util.javautil;

import java.util.Collections;
import java.util.Objects;

/**
 * 对象、数组、集合
 * Collections/Lists/Objects
 * java.util.Collections
 */
public class CollectionsTest{
    Collections collections;
    Objects objects;
    /**===========Collections==================
     // java.util包下的Collections类，该类主要用于操作集合或者返回集合
     // 降序
     Collections.reverse(list);
     // 获取最大值
     Integer max = Collections.max(list);
     // 将ArrayList转换成线程安全集合
     List<Integer> integers = Collections.synchronizedList(list);
     // 返回空集合
     Collections.emptyList();
     //二分查找
     int i = Collections.binarySearch(list, 3);
     // 转换成不可修改集合
     List<Integer> integers = Collections.unmodifiableList(list);

     *===========Lists==================
     // com.google.common.collect包下的集合工具：Lists 会更加好用
     // 快速初始化
     List<Integer> list = Lists.newArrayList(1, 2, 3);
     //将两个集合做笛卡尔积
     //插入理论笛卡尔乘积是一种数学运算，它从多个集合中返回一个集合（或乘积集合或简单地乘积）。
     //也就是说，对于集合A和B，笛卡尔乘积A×B是所有有序对(a,b)的集合
     List<List<Integer>> productList = Lists.cartesianProduct(list1,list2);
     // 分页
     List<List<Integer>> partitionList = Lists.partition(list, 2);
     // 流处理
     List<String> transformList = Lists.transform(list, x -> x.toUpperCase());
     // 反转
     List<Integer> reverseList = Lists.reverse(list);

     *===========Objects==================
     Objects.isNull(integer)
     Objects.nonNull(integer)
     //对象为空抛异常
     Objects.requireNonNull(integer1, () -> "参数不能为空");*/
}
