package com.fomp.note.util.springutil;

import org.springframework.util.Assert;
/**
 * 断言：org.springframework.util.Assert
 *     断言是一个逻辑判断，用于检查不应该发生的情况
 *     Assert 关键字在 JDK1.4 中引入，可通过 JVM 参数-enableassertions开启
 *     SpringBoot 中提供了 Assert 断言工具类，通常用于数据合法性检查@author: ymx
 */
public class AssertTest {

    public static void main(String[] args) {
        try {
            Assert.notNull("","参数不能为空！");
            Assert.isNull("","参数必须为空");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    /**
        ===========Collections==================
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

        ===========Lists==================
        // com.google.common.collect包下的集合工具：Lists 会更加好用
        // 快速初始化
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        //将两个集合做笛卡尔积
        List<List<Integer>> productList = Lists.cartesianProduct(list1,list2);
        // 分页
        List<List<Integer>> partitionList = Lists.partition(list, 2);
        // 流处理
        List<String> transformList = Lists.transform(list, x -> x.toUpperCase());
        // 反转
        List<Integer> reverseList = Lists.reverse(list);

        ===========Objects==================
        Objects.isNull(integer)
        Objects.nonNull(integer)
        // 对象为空抛异常
        Objects.requireNonNull(integer1, () -> "参数不能为空");
     */

}
