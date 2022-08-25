package com.fomp.note.java8newfeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream API的使用
 * 1、Stream API提供了一种高效且易于使用的处理数据的方式
 * 2、可以在Java层面去处理NoSql数据：查找、过滤、映射等
 * 3、Collection是一种静态的内存数据结构，面向内存，存储在内存中
 *   Stream是关于计算的，面向CPU，通过CPU实现计算
 */
public class StreamTest {
    public static void main(String[] args) {
    }
    /**
     * 1、创建Stream
     */
    public void createStream(){
        //获取Stream的方式：
        //1.通过集合
        List<String> list = new ArrayList<String>();
        Stream<String> stringStream1 = list.stream();//顺序流
        Stream<String> stringStream2 = list.parallelStream();//并行流
        //2.通过数组
        String[] str = new String[10];
        Stream<String> stringStream3 = Arrays.stream(str);
        //3.通过Stream类中的静态方法of()
        Stream<String> stream = Stream.of("a","b","c");
        //4.创建无限流
        //遍历前10个偶数
        Stream.iterate(0,t->t+2).limit(10).forEach(System.out::println);
        //生成
        //生成10个随机数
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }

    /**
     * “惰性求值”：是指多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，
     * 否则中间操作不会执行任何操作，而在终止操作时一次性全部处理。
     */
    /**
     * 2、中间操作：①筛选和切片
     */
    public void middleOperation1(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(4);
        list.add(4);
        list.add(5);
        //1.筛选和切片
        //filter(Predicate p)  接收Lambda，从流中排除某些元素
        Stream<Integer> stream = list.stream();
        stream.filter(i -> i>3).forEach(System.out::println);
        System.out.println("*********");
        //limit(n)  截断流，使其元素不超过给定数量
        list.stream().limit(4).forEach(System.out::println);
        System.out.println("*********");
        //skip(n) 返回扔掉前n个元素的流，不足n个，返回空的流
        list.stream().skip(4).forEach(System.out::println);
        System.out.println("*********");
        //distinct()  筛选，通过流所生成元素的hashCode()和equals()去除重复元素
        list.stream().distinct().forEach(System.out::println);
    }
    /**
     *2、中间操作：②映射
     */
    public void middleOperation2(){
        List<String> list = Arrays.asList("aa","bb","cc");
        //map(Function f)  接收一个函数参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
        System.out.println("**********************");
        List<Emp> emps = new Emp().getList();
        Stream<Integer> stream = emps.stream().map(Emp::getAge);
        stream.forEach(System.out::println);
        //练习
        Stream<Stream<Character>> streamStream1 = list.stream().map(str -> StreamTest.fromStringToStream(str));
        Stream<Stream<Character>> streamStream = list.stream().map(StreamTest::fromStringToStream);
        streamStream.forEach(s->{
            s.forEach(System.out::println);
        });
        //flatMap(Function f) 类似list.addALL()方法；map和flatMap的区别理解为list.add()和list.addALL()方法区别
        Stream<Character> characterStream = list.stream().flatMap(StreamTest::fromStringToStream);
        characterStream.forEach(System.out::println);
    }
    public static Stream<Character> fromStringToStream(String str){
        List<Character> list = new ArrayList<>();
        for (Character c:str.toCharArray()){
            list.add(c);
        }
        return list.stream();
    }

    /**
     *2、中间操作：③排序
     */
    public void middleOperation3(){
        List<String> list = Arrays.asList("1", "4", "3", "2");
        //sorted()  自然排序，升序
        list.stream().sorted().forEach(System.out::println);
        //sorted(Comparator c) 自定义排序
        List<Emp> emps = new Emp().getList();
        emps.stream().sorted((e1,e2)->Integer.compare(e1.getAge(),e2.getAge()))
                .forEach(System.out::println);
    }

    /**
     * （1）终端操作会从流的流水线生成结果。其结果可以是任何不是流的值，例如：List，Integer，甚至是void。
     * （2）流进行了终止操作后，不能再次使用。
     */
    /**
     * 3.终止操作：①匹配与查找
     */
    public void ctrlStream1(){
        List<Emp> emps = new Emp().getList();
        //boolean allMatch(Predicate p)-检查是否匹配所有元素
        //年龄18，16,20
        boolean b = emps.stream().allMatch(e -> e.getAge() > 18);
        System.out.println(b);
        //boolean anyMatch(Predicate p)-检查是否至少匹配一个元素
        boolean b1 = emps.stream().anyMatch(e -> e.getAge() > 20);
        System.out.println(b1);
        //boolean noneMatch(Predicate p)-检查是否没有匹配元素
        boolean b2 = emps.stream().noneMatch(e -> e.getAge() > 20);
        System.out.println(b2);
        System.out.println("*****************");
        //count()-返回流中元素的总数量
        long count = emps.stream().filter(e -> e.getAge() > 18).count();
        //max()-返回流中的最大值
        Stream<Integer> integerStream = emps.stream().map(e -> e.getAge());
        Optional<Integer> max = integerStream.max(Integer::compareTo);
        System.out.println(max);
    }
    /**
     * 3.终止操作：②规约
     */
    public void ctrlStream2(){
        //T reduce(T identity, BinaryOperator b)-可以将流中的值反复结合起来，得到一个值。返回T
        //计算1-10自然数的和
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        //第一个参数是初始值，0开始和是55,10开始是65
        Integer sum = list.stream().reduce(0, Integer::sum);
        System.out.println(sum);
        // Optional<T> reduce(BinaryOperator b);-可以将流中的值反复结合起来，得到一个值。返回Optional
        List<Emp> emps = new Emp().getList();
        Optional<Integer> ageSum = emps.stream().map(e -> e.getAge()).reduce(Integer::sum);
        System.out.println(ageSum);
    }
    /**
     * 3.终止操作：③收集
     */
    public void ctrlStream3(){
        //R collect(Collector c) 将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
        List<Emp> emps = new Emp().getList();
        List<Emp> empList = emps.stream().filter(e -> e.getAge() > 16).collect(Collectors.toList());
        empList.forEach(System.out::println);
    }

}

class Emp{
    private Integer age;
    private String name;
    public List<Emp> getList() {
        List<Emp> list = new ArrayList();
        Emp emp1 = new Emp(16,"张三");
        Emp emp2 = new Emp(13,"张三");
        Emp emp3 = new Emp(19,"张三");
        list.addAll(Arrays.asList(emp1,emp2,emp3));
        return list;
    }
    public Emp() {
    }
    public Emp(Integer age, String name) {
        this.age = age;
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
