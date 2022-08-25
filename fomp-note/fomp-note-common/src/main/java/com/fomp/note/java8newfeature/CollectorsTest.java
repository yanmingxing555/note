package com.fomp.note.java8newfeature;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * java.util.stream.Collectors实现各种有用的缩减操作的Collector的实现，
 * 例如将元素累积到集合中，根据各种标准汇总元素等。
 * 原文链接：https://blog.csdn.net/qq_43842093/article/details/122136492
 */
public class CollectorsTest {
    public static void main(String[] args) {
        testAveragingDouble();
    }
    /**
     * 1.averagingDouble方法返回一个Collector收集器，它生成应用于输入元素的double值函数的算术平均值。如果没有元素，则结果为0。
     * 注意：返回的平均值可能会因记录值的顺序而变化，这是由于除了不同大小的值之外，还存在累积舍入误差。
     *      通过增加绝对量排序的值(即总量，样本越大，结果越准确)往往会产生更准确的结果。
     *      如果任何记录的值是NaN或者总和在任何点NaN，那么平均值将是NaN。
     * 注意：double格式可以表示-253到253范围内的所有连续整数。如果管道有超过253的值，则平均计算中的除数将在253处饱和，从而导致额外的数值误差。
     */
    public static void testAveragingDouble(){
        //示例：统计所有学生的平均总成绩
        Double averagingDouble = menu.stream().collect(Collectors.averagingDouble(Student::getTotalScore));
        Optional.ofNullable(averagingDouble).ifPresent(System.out::println);
    }
    /**
     * 2.collectingAndThen方法调整Collector收集器以执行其它的结束转换。例如，可以调整toList()收集器，以始终生成一个不可变的列表
     */
    public static void testCollectingAndThen(){
        /*List<Student> studentList = menu.stream().collect(
                Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        System.out.println(studentList);*/
        //示例：以指定字符串The Average totalScore is->输出所有学生的平均总成绩
        Optional.ofNullable(menu.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.averagingInt(Student::getTotalScore), a -> "The Average totalScore is->" + a)
        )).ifPresent(System.out::println);
    }
    /**
     * 3.counting方法返回一个Collector收集器接受T类型的元素，用于计算输入元素的数量。如果没有元素，则结果为0。
     */
    public static void testCounting(){
        //示例：统计所有学生人数
        Optional.of(menu.stream().collect(Collectors.counting())).ifPresent(System.out::println);
    }
    /**
     * 4.1 groupingBy(Function)方法返回一个Collector收集器对T类型的输入元素执行"group by"操作，根据分类函数对元素进行分组，并将结果返回到Map。
     *
     * 注意：分类函数将元素映射到某些键类型K。收集器生成一个Map>，其键是将分类函数应用于输入元素得到的值，
     *      其对应值为List，其中包含映射到分类函数下关联键的输入元素。
     *      无法保证返回的Map或List对象的类型，可变性，可序列化或线程安全性。
     * 注意： 返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，
     *      这可能是一个昂贵的操作。如果不需要保留元素出现在生成的Map收集器中的顺序，
     *      则使用groupingByConcurrent(Function)可以提供更好的并行性能。
     */
    public static void testGroupingByFunction(){
        //示例：统计各个年级的学生信息
        Map<Student.GradeType, List<Student>> collect = menu.stream()
                .collect(Collectors.groupingBy(Student::getGradeType));
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * 4.2 groupingBy(Function, Collector)方法返回一个Collector收集器，
     *      对T类型的输入元素执行级联"group by"操作，根据分类函数对元素进行分组，
     *      然后使用指定的下游Collector收集器对与给定键关联的值执行缩减操作。
     * 注意：分类函数将元素映射到某些键类型K。下游收集器对T类型的元素进行操作，并生成D类型的结果。产生收集器生成Map<K, D>。
     *      返回的Map的类型，可变性，可序列化或线程安全性无法保证。
     * 注意： 返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，
     *      这可能是一个昂贵的操作。如果不需要保留向下游收集器提供元素的顺序，
     *      则使用groupingByConcurrent(Function, Collector)可以提供更好的并行性能。
     */
    public static void testGroupingByFunctionAndCollector() {
        //示例：统计各个年级的学生人数
        Optional.of(menu.stream()
                .collect(Collectors.groupingBy(Student::getGradeType, Collectors.counting())))
                .ifPresent(System.out::println);
    }
    /**
     * 4.3 groupingBy(Function, Supplier, Collector)方法返回一个Collector收集器，
     *      对T类型的输入元素执行级联"group by"操作，根据分类函数对元素进行分组，
     *      然后使用指定的下游Collector收集器对与给定键关联的值执行缩减操作。收集器生成的Map是使用提供的工厂函数创建的。
     * 注意：分类函数将元素映射到某些键类型K。下游收集器对T类型的元素进行操作，并生成D类型的结果。产生收集器生成Map<K, D>。
     * 注意： 返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，
     *      这可能是一个昂贵的操作。如果不需要保留向下游收集器提供元素的顺序，
     *      则使用groupingByConcurrent(Function, Supplier, Collector)可以提供更好的并行性能。
     */
    public static void testGroupingByFunctionAndSupplierAndCollector() {
        //示例：统计各个年级的平均成绩，并有序输出
        Map<Student.GradeType, Double> map = menu.stream()
                .collect(Collectors.groupingBy(
                        Student::getGradeType,//分组条件-key
                        TreeMap::new,//收集map
                        Collectors.averagingInt(Student::getTotalScore)));//value

        Optional.of(map.getClass()).ifPresent(System.out::println);
        Optional.of(map).ifPresent(System.out::println);
    }

    /**
     * 5.1groupingByConcurrent(Function)
     * groupingByConcurrent(Function)方法返回一个并发Collector收集器对T类型的输入元素执行"group by"操作，根据分类函数对元素进行分组
     *     这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     *     分类函数将元素映射到某些键类型K。收集器生成一个ConcurrentMap<K, List>，其键是将分类函数应用于输入元素得到的值，其对应值为List，其中包含映射到分类函数下关联键的输入元素。
     *     无法保证返回的Map或List对象的类型，可变性或可序列化，或者返回的List对象的线程安全性。
     *
     * 5.2groupingByConcurrent(Function, Collector)
     * groupingByConcurrent(Function, Collector)方法返回一个并发Collector收集器，对T类型的输入元素执行级联"group by"操作，根据分类函数对元素进行分组，然后使用指定的下游Collector收集器对与给定键关联的值执行缩减操作。
     *     这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     *     分类函数将元素映射到某些键类型K。下游收集器对T类型的元素进行操作，并生成D类型的结果。产生收集器生成Map<K, D>。
     *
     * 5.3groupingByConcurrent(Function, Supplier, Collector)
     * groupingByConcurrent(Function, Supplier, Collector)方法返回一个并行Collector收集器，对T类型的输入元素执行级联"group by"操作，根据分类函数对元素进行分组，然后使用指定的下游Collector收集器对与给定键关联的值执行缩减操作。收集器生成的ConcurrentMap是使用提供的工厂函数创建的。
     *     这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     *     分类函数将元素映射到某些键类型K。下游收集器对T类型的元素进行操作，并生成D类型的结果。产生收集器生成Map<K, D>。
     */
    /**
     * 6.1 joining()方法返回一个Collector收集器，它按遇见顺序将输入元素连接成String。
     */
    public static void testJoining() {
        //示例：将所有学生的姓名连接成字符串
        Optional.of(menu.stream().map(Student::getName).collect(Collectors.joining()))
                .ifPresent(System.out::println);
    }

    /**
     * 6.2 joining(delimiter)方法返回一个Collector收集器，它以遇见顺序连接由指定分隔符分隔的输入元素。
     */
    public static void testJoiningWithDelimiter() {
        //示例：将所有学生的姓名以","分隔连接成字符串
        Optional.of(menu.stream().map(Student::getName).collect(Collectors.joining(",")))
                .ifPresent(System.out::println);
    }
    /**
     * 6.3 joining(delimiter, prefix, suffix)方法返回一个Collector收集器，它以遇见顺序将由指定分隔符分隔的输入元素与指定的前缀和后缀连接起来。
     */
    public static void testJoiningWithDelimiterAndPrefixAndSuffix() {
        //示例：将所有学生的姓名以","分隔，以Names[为前缀，]为后缀连接成字符串
        Optional.of(menu.stream().map(Student::getName).collect(Collectors.joining(",", "Names[", "]")))
                .ifPresent(System.out::println);
    }

    /**
     * 7.mapping方法通过在累积之前将映射函数应用于每个输入元素，将Collector收集器接受U类型的元素调整为一个接受T类型的元素。
     */
    public static void testMapping() {
        //示例：将所有学生的姓名以","分隔连接成字符串
        Optional.of(menu.stream().collect(Collectors.mapping(Student::getName, Collectors.joining(","))))
                .ifPresent(System.out::println);
    }
    /**
     * 8.maxBy方法返回一个Collector收集器，它根据给定的Comparator比较器生成最大元素，描述为Optional。
     */
    public static void testMaxBy() {
        //示例：列出所有学生中成绩最高的学生信息
        menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Student::getTotalScore)))
                .ifPresent(System.out::println);
    }
    /**
     * 9.minBy方法返回一个Collector收集器，它根据给定的Comparator比较器生成最小元素，描述为Optional。
     */
    public static void testMinBy() {
        menu.stream().collect(Collectors.minBy(Comparator.comparingInt(Student::getTotalScore)))
                .ifPresent(System.out::println);
    }

    /**
     * 10.1 partitioningBy(Predicate)方法返回一个Collector收集器，它根据Predicate对输入元素进行分区，并将它们组织成Map>。
     * 返回的Map的类型，可变性，可序列化或线程安全性无法保证。
     */
    public static void testPartitioningByWithPredicate() {
        //示例：列出所有学生中本地和非本地学生信息
        Map<Boolean, List<Student>> collect = menu.stream()
                .collect(Collectors.partitioningBy(Student::isLocal));
        Optional.of(collect).ifPresent(System.out::println);
//{false=[Student{name='王五', totalScore=483, local=false, gradeType=THREE}, Student{name='孙七', totalScore=499, local=false, gradeType=ONE}], true=[Student{name='刘一', totalScore=721, local=true, gradeType=THREE}, Student{name='陈二', totalScore=637, local=true, gradeType=THREE}, Student{name='张三', totalScore=666, local=true, gradeType=THREE}, Student{name='李四', totalScore=531, local=true, gradeType=TWO}, Student{name='赵六', totalScore=367, local=true, gradeType=THREE}]}
    }
    /**
     * 10.2 partitioningBy(Predicate, Collector)方法返回一个Collector收集器，它根据Predicate对输入元素进行分区，
     *      根据另一个Collector收集器减少每个分区中的值，并将它们组织成Map，其值是下游减少的结果。
     *  返回的Map的类型，可变性，可序列化或线程安全性无法保证。
     */
    public static void testPartitioningByWithPredicateAndCollector() {
        //示例：列出所有学生中本地和非本地学生的平均总成绩
        Map<Boolean, Double> collect = menu.stream()
                .collect(Collectors.partitioningBy(
                        Student::isLocal,
                        Collectors.averagingInt(Student::getTotalScore)));
        Optional.of(collect).ifPresent(System.out::println);
        // {false=491.0, true=584.4}
    }
    /**
     * 11.1 返回一个Collector收集器，它在指定的BinaryOperator下执行其输入元素的缩减。结果被描述为Optional
     * reducing()相关收集器在groupingBy或partitioningBy下游的多级缩减中使用时非常有用。要对流执行简单缩减，请使用Stream#reduce(BinaryOperator)。
     */
    public static void testReducingBinaryOperator() {
        //示例：列出所有学生中成绩最高的学生信息
        menu.stream().collect(Collectors.reducing(
                BinaryOperator.maxBy(
                        Comparator.comparingInt(Student::getTotalScore))))
                .ifPresent(System.out::println);
        // Student{name='刘一', totalScore=721, local=true, gradeType=THREE}
    }
    //可直接使用reduce
    public static void testReducingBinaryOperator1() {
        //示例：列出所有学生中成绩最高的学生信息
        menu.stream().reduce(BinaryOperator.maxBy(
                Comparator.comparingInt(Student::getTotalScore)))
                .ifPresent(System.out::println);
        //Student{name='刘一', totalScore=721, local=true, gradeType=THREE}
    }
    /**
     * 11.2 返回一个Collector收集器，它使用提供的标识在指定的BinaryOperator下执行其输入元素的缩减。
     * reducing()相关收集器在groupingBy或partitioningBy下游的多级缩减中使用时非常有用。要对流执行简单缩减，请使用Stream#reduce(Object, BinaryOperator)。
     */
    public static void testReducingBinaryOperatorAndIdentiy() {
        //示例：统计所有学生的总成绩
        Integer result = menu.stream()
                .map(Student::getTotalScore)
                .collect(Collectors.reducing(0, (d1, d2) -> d1 + d2));
        System.out.println(result);
        // 3904
    }
    //可直接使用reduce
    public static void testReducingBinaryOperatorAndIdentiy1() {
        Integer result = menu.stream()
                .map(Student::getTotalScore).reduce(0, (d1, d2) -> d1 + d2);
        System.out.println(result);
    }
    /**
     * 11.3 返回一个Collector收集器，它在指定的映射函数和BinaryOperator下执行其输入元素的缩减。
     *      这是对reducing(Object, BinaryOperator)的概括，它允许在缩减之前转换元素。
     * reducing()相关收集器在groupingBy或partitioningBy下游的多级缩减中使用时非常有用。要对流执行简单缩减，
     * 请使用Stream#map(Function)和Stream#reduce(Object, BinaryOperator)。
     */
    public static void testReducingBinaryOperatorAndIdentiyAndFunction() {
        //示例：统计所有学生的总成绩
        Integer result = menu.stream()
                .collect(Collectors.reducing(0, Student::getTotalScore, (d1, d2) -> d1 + d2));
        System.out.println(result);
    }
    //可直接使用reduce
    public static void testReducingBinaryOperatorAndIdentiyAndFunction1() {
        Integer result = menu.stream()
                .map(Student::getTotalScore)
                .reduce(0, (d1, d2) -> d1 + d2);
        System.out.println(result);
    }
    /**
     * 12.summarizingDouble方法返回一个Collector收集器，它将double生成映射函数应用于每个输入元素，并返回结果值的摘要统计信息。
     */
    public static void testSummarizingInt() {
        //示例：统计所有学生的摘要信息（总人数，总成绩，最高成绩，最低成绩和平均成绩）
        DoubleSummaryStatistics result = menu.stream()
                .collect(Collectors.summarizingDouble(Student::getTotalScore));
        Optional.of(result).ifPresent(System.out::println);
        // DoubleSummaryStatistics{count=7, sum=3904.000000, min=367.000000, average=557.714286, max=721.000000}
        // IntSummaryStatistics{count=7, sum=3904, min=367, average=557.714286, max=721}
        // LongSummaryStatistics{count=7, sum=3904, min=367, average=557.714286, max=721}
    }

    /**
     * 13.summingDouble返回一个Collector收集器，它生成应用于输入元素的double值函数的总和。如果没有元素，则结果为0。
     * 返回的总和可能会因记录值的顺序而变化，这是由于除了不同大小的值之外，还存在累积舍入误差。通
     * 过增加绝对量排序的值(即总量，样本越大，结果越准确)往往会产生更准确的结果。
     * 如果任何记录的值是NaN或者总和在任何点NaN，那么总和将是NaN。
     */
    public static void testSummingDouble() {
        //示例：统计所有学生的总成绩
        Optional.of(menu.stream().collect(Collectors.summingDouble(Student::getTotalScore)))
                .ifPresent(System.out::println);
        // 3904.0
        // 3904
        // 3904
    }

    /**
     * 14.返回一个Collector收集器，它按遇见顺序将输入元素累积到一个新的Collection收集器中。Collection收集器由提供的工厂创建。
     */
    public static void testToCollection() {
        //示例：统计总分大于600的所有学生的信息放入LinkedList中
        Optional.of(menu.stream().filter(d -> d.getTotalScore() > 600)
                .collect(Collectors.toCollection(LinkedList::new)))
                .ifPresent(v -> {
                    System.out.println(v.getClass());
                    System.out.println(v);
                });
        // class java.util.LinkedList
        // [Student{name='刘一', totalScore=721, local=true, gradeType=THREE}, Student{name='陈二', totalScore=637, local=true, gradeType=THREE}, Student{name='张三', totalScore=666, local=true, gradeType=THREE}]
    }

    /**
     * 15.1 toConcurrentMap(Function, Function)
     * 返回一个并发的Collector收集器，它将元素累积到ConcurrentMap中，其键和值是将提供的映射函数应用于输入元素的结果
     *
     * 如果映射的键包含重复项(根据Object#equals(Object))，则在执行收集操作时会抛出IllegalStateException。
     * 如果映射的键可能有重复，请使用toConcurrentMap(Function, Function, BinaryOperator)。
     * 注意： 键或值作为输入元素是常见的。在这种情况下，实用方法java.util.function.Function#identity()可能会有所帮助。
     * 这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     */
    public static void testToConcurrentMap() {
        //示例：以学生姓名为键总分为值统计信息
        Optional.of(menu.stream()
                .collect(Collectors.toConcurrentMap(Student::getName, Student::getTotalScore)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
        // {李四=531, 孙七=499, 刘一=721, 张三=666, 陈二=637, 王五=483, 赵六=367}
        // class java.util.concurrent.ConcurrentHashMap
    }
    /**
     * 15.2 toConcurrentMap(Function, Function, BinaryOperator)
     * 返回一个并发的Collector收集器，它将元素累积到ConcurrentMap中，其键和值是将提供的映射函数应用于输入元素的结果。
     *
     * 如果映射的键包含重复项(根据Object#equals(Object))，则将值映射函数应用于每个相等的元素，并使用提供的合并函数合并结果。
     * 注意： 有多种方法可以处理映射到同一个键的多个元素之间的冲突。toConcurrentMap的其它形式只是使用无条件抛出的合并函数，但你可以轻松编写更灵活的合并策略。
     * 例如，如果你有一个Person流，并且你希望生成一个“电话簿”映射名称到地址，但可能有两个人具有相同的名称，你可以按照以下方式进行优雅的处理这些冲突，
     * 并生成一个Map将名称映射到连接的地址列表中：
     * Map<String, String> phoneBook
     *     people.stream().collect(toConcurrentMap(Person::getName,
     *                                             Person::getAddress,
     *                                             (s, a) -> s + ", " + a));
     *  这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     */
    public static void testToConcurrentMapWithBinaryOperator() {
        //示例：以年级为键学生人数为值统计信息
        Optional.of(menu.stream()
                //.collect(Collectors.toConcurrentMap(Student::getGradeType, v -> 1L, (a, b) -> a + b)))
                .collect(Collectors.toConcurrentMap(Student::getGradeType, Student::getName, (a, b) -> a +","+ b)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
        // {ONE=1, THREE=5, TWO=1}
        // class java.util.concurrent.ConcurrentHashMap
    }

    /**
     * 15.3 toConcurrentMap(Function, Function, BinaryOperator, Supplier)
     * 返回一个并发的Collector收集器，它将元素累积到ConcurrentMap中，其键和值是将提供的映射函数应用于输入元素的结果。
     *
     * 如果映射的键包含重复项(根据Object#equals(Object))，则将值映射函数应用于每个相等的元素，
     * 并使用提供的合并函数合并结果。ConcurrentMap由提供的供应商函数创建。
     * 这是一个Collector.Characteristics#CONCURRENT并发和Collector.Characteristics#UNORDERED无序收集器。
     */
    public static void testToConcurrentMapWithBinaryOperatorAndSupplier() {
        Optional.of(menu.stream()
                .collect(Collectors.toConcurrentMap(
                        Student::getGradeType,
                        v -> 1L,
                        (a, b) -> a + b,
                        ConcurrentSkipListMap::new)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
        // {ONE=1, TWO=1, THREE=5}
        // class java.util.concurrent.ConcurrentSkipListMap
    }

    /**
     * 16.toList返回一个Collector收集器，它将输入元素累积到一个新的List中。返回的List的类型，可变性，
     * 可序列化或线程安全性无法保证;如果需要更多地控制返回的List，请使用toCollection(Supplier)。
     */
    public static void testToList() {
        //示例：查出本地学生的信息并放入ArrayList中
        Optional.of(menu.stream().filter(Student::isLocal).collect(Collectors.toList()))
                .ifPresent(r -> {
                    System.out.println(r.getClass());
                    System.out.println(r);
                });
        // class java.util.ArrayList
        // [Student{name='刘一', totalScore=721, local=true, gradeType=THREE}, Student{name='陈二', totalScore=637, local=true, gradeType=THREE}, Student{name='张三', totalScore=666, local=true, gradeType=THREE}, Student{name='李四', totalScore=531, local=true, gradeType=TWO}, Student{name='赵六', totalScore=367, local=true, gradeType=THREE}]
    }

    /**
     * 17.1  toMap(Function, Function)
     *      返回一个Collector收集器，它将元素累积到Map中，其键和值是将提供的映射函数应用于输入元素的结果。
     *      如果映射的键包含重复项(根据Object#equals(Object))，则在执行收集操作时会抛出IllegalStateException。
     *      如果映射的键可能有重复，请使用toMap(Function, Function, BinaryOperator)。
     *    注意： 键或值作为输入元素是常见的。在这种情况下，实用方法java.util.function.Function#identity()可能会有所帮助。
     *      返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，这可能是一个昂贵的操作。
     *      如果不需要将结果以遇见的顺序插入Map，则使用toConcurrentMap(Function, Function)可以提供更好的并行性能。
     * 17.2  toMap(Function, Function, BinaryOperator)
     *      返回一个并发的Collector收集器，它将元素累积到Map中，其键和值是将提供的映射函数应用于输入元素的结果。
     *     如果映射的键包含重复项(根据Object#equals(Object))，则将值映射函数应用于每个相等的元素，并使用提供的合并函数合并结果。
     *   注意： 有多种方法可以处理映射到同一个键的多个元素之间的冲突。toMap的其它形式只是使用无条件抛出的合并函数，但你可以轻松编写更灵活的合并策略。
     *     例如，如果你有一个Person流，并且你希望生成一个“电话簿”映射名称到地址，但可能有两个人具有相同的名称，你可以按照以下方式进行优雅的处理这些冲突，
     *     并生成一个Map将名称映射到连接的地址列表中：
     *     Map<String, String> phoneBook
     *      people.stream().collect(toConcurrentMap(Person::getName,
     *                                              Person::getAddress,
     *                                              (s, a) -> s + ", " + a));
     *     返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，这可能是一个昂贵的操作。
     *     如果不需要将结果以遇见的顺序插入Map，则使用toConcurrentMap(Function, Function, BinaryOperator)可以提供更好的并行性能。
     * 17.3  toMap(Function, Function, BinaryOperator, Supplier)
     *      返回一个并发的Collector收集器，它将元素累积到Map中，其键和值是将提供的映射函数应用于输入元素的结果。
     *     如果映射的键包含重复项(根据Object#equals(Object))，则将值映射函数应用于每个相等的元素，并使用提供的合并函数合并结果。Map由提供的供应商函数创建。
     *   注意： 返回的Collector收集器不是并发的。对于并行流管道，combiner函数通过将键从一个映射合并到另一个映射来操作，这可能是一个昂贵的操作。
     *     如果不需要将结果以遇见的顺序插入Map，则使用toConcurrentMap(Function, Function, BinaryOperator, Supplier)可以提供更好的并行性能。
     */
    /**
     * 18.toSet返回一个Collector收集器，它将输入元素累积到一个新的Set中。返回的Set的类型，可变性，可序列化或线程安全性无法保证;
     * 如果需要更多地控制返回的Set，请使用toCollection(Supplier)。
     */
    public static void testToSet() {
        //示例：查出本地学生的信息并放入HashSet中
        Optional.of(menu.stream().filter(Student::isLocal).collect(Collectors.toSet()))
                .ifPresent(r -> {
                    System.out.println(r.getClass());
                    System.out.println(r);
                });
        // class java.util.HashSet
        // [Student{name='张三', totalScore=666, local=true, gradeType=THREE}, Student{name='陈二', totalScore=637, local=true, gradeType=THREE}, Student{name='刘一', totalScore=721, local=true, gradeType=THREE}, Student{name='李四', totalScore=531, local=true, gradeType=TWO}, Student{name='赵六', totalScore=367, local=true, gradeType=THREE}]
    }
    public static List<Student> initStudents(){
        return Arrays.asList(
                new Student("刘一", 721, true, Student.GradeType.THREE),
                new Student("陈二", 637, true, Student.GradeType.THREE),
                new Student("张三", 666, true, Student.GradeType.THREE),
                new Student("李四", 531, true, Student.GradeType.TWO),
                new Student("王五", 483, false, Student.GradeType.THREE),
                new Student("赵六", 367, true, Student.GradeType.THREE),
                new Student("孙七", 499, false, Student.GradeType.ONE));
    }
    private static List<Student> menu = initStudents();
}
/**
 * 学生信息
 */
class Student {
    /** 姓名 */
    private String name;
    /** 总分 */
    private int totalScore;
    /** 是否本地人 */
    private boolean local;
    /** 年级 */
    private GradeType gradeType;
    /**
     * 年级类型
     */
    public enum GradeType {ONE,TWO,THREE}
    public Student(String name, int totalScore, boolean local, GradeType gradeType) {
        this.name = name;
        this.totalScore = totalScore;
        this.local = local;
        this.gradeType = gradeType;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", totalScore=" + totalScore +
                ", local=" + local +
                ", gradeType=" + gradeType +
                '}';
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    public boolean isLocal() {
        return local;
    }
    public void setLocal(boolean local) {
        this.local = local;
    }
    public GradeType getGradeType() {
        return gradeType;
    }
    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
    }
}