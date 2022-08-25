package com.fomp.note.designmode.$01singleton;

class Singleton {
    /**
     * 单例模式：Singleton
     * 模式定义: 保证一个类只有一个实例，并且提供一个全局访问点
     * 场景: 重量级的对象，不需要多个实例，如线程池，数据库连接池。
     * 源码中的应用：
     *      1 Spring & JDK
     *      2 java.lang.Runtime
     *      3 org.springframework.aop.framework.ProxyFactoryBean
     *      4 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
     *      5 org.springframework.core.ReactiveAdapterRegistry
     *      6 Tomcat
     *      7 org.apache.catalina.webresources.TomcatURLStreamHandlerFactory
     *      8 反序列化指定数据源
     *      9 java.util.Currency
     */
}
/**
 * 1.懒汉模式：延迟加载， 只有在真正使用的时候，才开始实例化。
 *  1）线程安全问题
 *  2）double check 加锁优化
 *  3）编译器(JIT),CPU 有可能对指令进行重排序，导致使用到尚未初始化的实例，
 *      可以通过添加volatile 关键字进行修饰， 对于volatile 修饰的字段，可以防止指令重排。
 */
class LazySingleton {

    private static volatile LazySingleton instance;
    private LazySingleton() {
    }
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                    // 字节码层
                    // JIT，CPU 有可能对如下指令进行重排序
                    // 1 .分配空间
                    // 2 .初始化
                    // 3 .引用赋值
                    // 如重排序后的结果为如下
                    // 1 .分配空间
                    // 3 .引用赋值 如果在当前指令执行完，有其他线程来获取实例，将拿到尚未初始化好的 实例
                    // 2 .初始化
                }
            }
        }
        return instance;
    }
}

/**
 * 2.饿汉模式：类加载的初始化阶段就完成了实例的初始化。
 *   本质上就是借助于jvm 类加载机制，保证实例的唯一性（初始化过程只会执行一次）
 *   及线程安全（JVM以同步的形式来完成类加载的整个过程）。
 * 类加载过程：
 * 1，加载二进制数据到内存中，生成对应的Class数据结构，
 * 2，连接： a. 验证， b.准备（给类的静态成员变量赋默认值），c.解析
 * 3，初始化： 给类的静态变量赋初值 只有在真正使用对应的类时，才会触发初始化
 *      如（ 当前类是启动类即 main函数所在类，直接进行new 操作，访问静态属性、访问静态方 法，用反射访问类，初始化一个类的子类等.）
 */
class HungrySingleton{

    private static HungrySingleton instance = new HungrySingleton();
    private HungrySingleton(){
    }
    public static HungrySingleton getInstance(){
        return instance;
    }
}



