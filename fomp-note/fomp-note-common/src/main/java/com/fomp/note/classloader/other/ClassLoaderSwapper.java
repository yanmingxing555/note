package com.fomp.note.classloader.other;

import java.lang.reflect.Method;

/**
 *
 * 为避免jar冲突，比如hbase可能有多个版本的读写依赖jar包
 * 就需要脱离当前classLoader去加载这些jar包，执行完成后，又退回到原来classLoader上继续执行接下来的代码
 */
public final class ClassLoaderSwapper {
    private ClassLoader storeClassLoader = null;

    private ClassLoaderSwapper() {
    }

    public static ClassLoaderSwapper newCurrentThreadClassLoaderSwapper() {
        return new ClassLoaderSwapper();
    }

    /**
     * 保存当前classLoader，并将当前线程的classLoader设置为所给classLoader
     *
     * @param
     * @return
     */
    public ClassLoader setCurrentThreadClassLoader(ClassLoader classLoader) {
        this.storeClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        return this.storeClassLoader;
    }

    /**
     * 将当前线程的类加载器设置为保存的类加载
     * @return
     */
    public ClassLoader restoreCurrentThreadClassLoader() {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.storeClassLoader);
        return classLoader;
    }

    public static void main(String[] args) throws Exception{
        String jar1 = "/Users/chiangtaol/Downloads/test/jd/jar1"; //自己定义的测试jar包，不同版本打印内容不同
        String jar2 = "/Users/chiangtaol/Downloads/test/jd/jar2";

        JarLoader jarLoader = new JarLoader(new String[]{jar1});
        ClassLoaderSwapper classLoaderSwapper = ClassLoaderSwapper.newCurrentThreadClassLoaderSwapper();
        classLoaderSwapper.setCurrentThreadClassLoader(jarLoader);
        Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("cn.tnt.bean.TestClass");
        classLoaderSwapper.restoreCurrentThreadClassLoader();
        Object o = aClass.newInstance();
        Method isEmptyMethod = aClass.getDeclaredMethod("hello");
        Object invoke = isEmptyMethod.invoke(o);
        System.out.println(invoke);


        JarLoader jarLoader2 = new JarLoader(new String[]{jar2});
        ClassLoaderSwapper classLoaderSwapper2 = ClassLoaderSwapper.newCurrentThreadClassLoaderSwapper();
        classLoaderSwapper2.setCurrentThreadClassLoader(jarLoader2);
        Class<?> aClass2 = Thread.currentThread().getContextClassLoader().loadClass("cn.tnt.bean.TestClass");
        classLoaderSwapper.restoreCurrentThreadClassLoader();
        Object o2 = aClass2.newInstance();
        Method isEmptyMethod2 = aClass2.getDeclaredMethod("hello");
        Object invoke2 = isEmptyMethod2.invoke(o2);
        System.out.println(invoke2);
    }
}
