package com.fomp.note.util.springutil;

import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射
 */
public class ReflectionUtilsTest {
    public static void main(String[] args) throws Exception{
        System.out.println("---------- findMethod ----------");
        // 获取方法
        Method method1 = ReflectionUtils.findMethod(DemoObject.class, "method01");
        Method method2 = ReflectionUtils.findMethod(DemoObject.class, "method02");
        // public java.lang.Object com.zstu.student.DemoObject.method01()
        System.out.println(method1);
        System.out.println("---------- findField ----------");
        // 获取属性
        Field field1 = ReflectionUtils.findField(DemoObject.class, "field01");
        Field field2 = ReflectionUtils.findField(DemoObject.class, "field02");
        // private java.lang.String com.zstu.student.DemoObject.field01
        System.out.println(field1);
        System.out.println("---------- accessibleConstructor ----------");
        // 获取构造方法
        Constructor<DemoObject> constructor1 = ReflectionUtils.accessibleConstructor(DemoObject.class);
        Constructor<DemoObject> constructor2 = ReflectionUtils.accessibleConstructor(DemoObject.class, String.class);
        // [public com.zstu.student.DemoObject(), public com.zstu.student.DemoObject(java.lang.String)]
        System.out.println(Arrays.toString(new String[] { constructor1.toString(), constructor2.toString() }));
        System.out.println("---------- declaresException ----------");
        // 方法是否存在指定的抛出异常
        assert method2 != null;
        boolean existIOException = ReflectionUtils.declaresException(method2, IOException.class);
        boolean existException = ReflectionUtils.declaresException(method2, Exception.class);
        // true - false
        System.out.println(existIOException + " - " + existException);
        System.out.println("---------- doWithFields ----------");
        // 返回所有字段，通过回调
        //private java.lang.String com.zstu.student.DemoObject.field01
        //public static final java.lang.String com.zstu.student.DemoObject.field02
        ReflectionUtils.doWithFields(DemoObject.class, System.out::println);
        System.out.println("---------- doWithLocalMethods ----------");
        // 返回当前类所有方法，通过回调
        ReflectionUtils.doWithLocalMethods(DemoObject.class, System.out::println);
        System.out.println("---------- doWithMethods ----------");
        // 返回所有方法包括继承类，通过回调
        ReflectionUtils.doWithMethods(DemoObject.class, System.out::println);
        System.out.println("---------- getAllDeclaredMethods ----------");
        // 返回所有方法包括继承
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(DemoObject.class);
        System.out.println(Arrays.toString(methods));
        System.out.println("---------- getDeclaredMethods ----------");
        // 返回所有当前类的方法
        //methods = ReflectionUtils.getDeclaredMethods(DemoObject.class);
        System.out.println(Arrays.toString(methods));
        System.out.println("---------- doWithLocalFields ----------");
        // 当前类的所有方法，通过回调
        // private java.lang.String com.zstu.student.DemoObject.field01
        // public static final java.lang.String com.zstu.student.DemoObject.field02
        ReflectionUtils.doWithLocalFields(DemoObject.class, System.out::println);
        System.out.println("---------- getUniqueDeclaredMethods ----------");
        // 若在子类重新父类方法则该方法将被移除
        methods = ReflectionUtils.getUniqueDeclaredMethods(DemoObject.class);
        System.out.println(Arrays.toString(methods));
        System.out.println("---------- getField ----------");
        // 获取字段的值
        Object ret1 = ReflectionUtils.getField(field2, new DemoObject());
        // 这是字段2
        System.out.println(ret1);
        System.out.println("---------- invokeMethod ----------");
        // 调用方法
        Object ret2 = ReflectionUtils.invokeMethod(method1, new DemoObject());
        // 无参方法1
        System.out.println(ret2);
        System.out.println("---------- isxxx----------");
        // 是toString方法吗? true是,false不是
        boolean isString = ReflectionUtils.isToStringMethod(ReflectionUtils.findMethod(DemoObject.class, "toString"));
        // true
        System.out.println(isString);
        // 是否公共静态final修饰属性吗? true是,false不是
        boolean isPublicStatic = ReflectionUtils.isPublicStaticFinal(field2);
        // true
        System.out.println(isPublicStatic);
        // 是Object类声明的方法吗? true是,false不是
        boolean isObject = ReflectionUtils.isObjectMethod(ReflectionUtils.findMethod(DemoObject.class, "toString"));
        // true
        System.out.println(isObject);
        // 是equals方法吗? true是,false不是
        boolean isEquals = ReflectionUtils.isEqualsMethod(ReflectionUtils.findMethod(DemoObject.class, "equals", Object.class));
        // true
        System.out.println(isEquals);
        // 是hashCode方法吗? true是,false不是
        boolean isHashCode = ReflectionUtils.isHashCodeMethod(ReflectionUtils.findMethod(DemoObject.class, "hashCode"));
        // true
        System.out.println(isHashCode);
        // 清空缓存，每次查询(方法,参数)时都会做缓存。
        ReflectionUtils.clearCache();
    // 是Cglib重命名的方法吗? TODO test
    // ReflectionUtils.isCglibRenamedMethod()
    }
    /**
     1、获取方法
         // 在类中查找指定方法
         Method findMethod(Class<?> clazz, String name)
         // 同上，额外提供方法参数类型作查找条件
         Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes)
         // 获得类中所有方法，包括继承而来的
         Method[] getAllDeclaredMethods(Class<?> leafClass)
         // 在类中查找指定构造方法
         Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
         // 是否是 equals() 方法
         boolean isEqualsMethod(Method method)
         // 是否是 hashCode() 方法
         boolean isHashCodeMethod(Method method)
         // 是否是 toString() 方法
         boolean isToStringMethod(Method method)
         // 是否是从 Object 类继承而来的方法
         boolean isObjectMethod(Method method)
         // 检查一个方法是否声明抛出指定异常
         boolean declaresException(Method method, Class<?> exceptionType)
     2、执行方法
         // 执行方法
         Object invokeMethod(Method method, Object target)
         // 同上，提供方法参数
         Object invokeMethod(Method method, Object target, Object... args)
         // 取消 Java 权限检查。以便后续执行该私有方法
         void makeAccessible(Method method)
         // 取消 Java 权限检查。以便后续执行私有构造方法
         void makeAccessible(Constructor<?> ctor)
     3、获取字段
         // 在类中查找指定属性
         Field findField(Class<?> clazz, String name)
         // 同上，多提供了属性的类型
         Field findField(Class<?> clazz, String name, Class<?> type)
         // 是否为一个 "public static final" 属性
         boolean isPublicStaticFinal(Field field)
     4、设置字段
         // 获取 target 对象的 field 属性值
         Object getField(Field field, Object target)
         // 设置 target 对象的 field 属性值，值为 value
         void setField(Field field, Object target, Object value)
         // 同类对象属性对等赋值
         void shallowCopyFieldState(Object src, Object dest)
         // 取消 Java 的权限控制检查。以便后续读写该私有属性
         void makeAccessible(Field field)
         // 对类的每个属性执行 callback
         void doWithFields(Class<?> clazz, ReflectionUtils.FieldCallback fc)
         // 同上，多了个属性过滤功能。
         void doWithFields(Class<?> clazz, ReflectionUtils.FieldCallback fc,
         ReflectionUtils.FieldFilter ff)
         // 同上，但不包括继承而来的属性
         void doWithLocalFields(Class<?> clazz, ReflectionUtils.FieldCallback fc)
     */
}
class DemoObject {
    private String field01 = "这是字段1";
    public static final String field02 = "这是字段2";
    public DemoObject() {
    }
    public DemoObject(String field01) {
        this.field01 = field01;
    }
    public Object method01() {
        return "无参方法1";
    }
    public void method01(String name) {
        System.out.println("有参方法1" + name);
    }
    public void method02() throws IOException {
        System.out.println("异常方法2");
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
