package com.fomp.note.classloader.other;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author YSS-YMX
 */
public class SADKClassLoader  extends URLClassLoader {

    public final static String jar_ZSB_Path = "D:/projects/testSDK/lib/SADK3631Patch-3.0.2.0.jar";
    public final static String jar_PAB_Path = "D:/projects/testSDK/lib/SADK-3.2.1.3.jar";
    private static volatile SADKClassLoader instance;

    public synchronized static SADKClassLoader getInstance(URL[] urls, ClassLoader parent){
        if(instance == null){
            instance = new SADKClassLoader(urls, parent);
        }
        return instance;
    }

    private SADKClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class c = super.findLoadedClass(name);
        if(c != null){
            return c;
        }
        if (name.indexOf("sadk") >= 1) {
            return findClass(name);
        }
        return super.loadClass(name);
    }

    public static void test() throws Exception {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        URL[] zsbUrls = new URL[]{new File(jar_ZSB_Path).toURL()};
        SADKClassLoader zsbLoader = SADKClassLoader.getInstance(zsbUrls, oldClassLoader);
        Thread.currentThread().setContextClassLoader(zsbLoader);
        Class<?> zsb_Class = zsbLoader.loadClass("cfca.sadk.lib.crypto.JCrypto");
        System.out.println(zsb_Class);
        String zsbJarName = zsb_Class.getProtectionDomain().getCodeSource().getLocation().getFile();
        System.out.println(zsbJarName);

        URL[] pabUrls = new URL[]{new File(jar_PAB_Path).toURL()};
        SADKClassLoader pabLoader = SADKClassLoader.getInstance(pabUrls, oldClassLoader);
        Thread.currentThread().setContextClassLoader(pabLoader);
        Class<?> pab_Class = pabLoader.loadClass("cfca.sadk.lib.crypto.JCrypto");
        System.out.println(pab_Class);
        String pabJarName = pab_Class.getProtectionDomain().getCodeSource().getLocation().getFile();
        System.out.println(pabJarName);

    }

    public static void main(String[] args) throws Exception {
        //test();
        URLClassLoader systemClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = oldClassLoader.getSystemClassLoader();
        URL[] zsbUrls = new URL[]{new File(jar_ZSB_Path).toURL()};
        SADKClassLoader zsbLoader = SADKClassLoader.getInstance(zsbUrls, oldClassLoader);
        System.out.println(oldClassLoader);
        System.out.println(classLoader);
        System.out.println(zsbLoader);
        System.out.println(zsbLoader.getParent());
        System.out.println(oldClassLoader==classLoader);







        /*URL[] urls = new URL[]{new File(jarPath).toURL()};
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        SADKClassLoader sadk = new SADKClassLoader(urls, oldClassLoader);
        Class<?> aClass = sadk.loadClass("cfca.sadk.lib.crypto.JCrypto");
        System.out.println(aClass);

        URL[] urls1 = new URL[]{new File(xsPath).toURL()};
        SADKClassLoader sadk1 = new SADKClassLoader(urls, oldClassLoader);
        Class<?> aClass1 = sadk1.loadClass("cfca.sadk.lib.crypto.JCrypto");
        System.out.println(aClass1);
        System.out.println(aClass == aClass1);

//        Method init = aClass.getMethod("init", new Class[]{String.class, String.class});
//        init.invoke(null,"/opt/pay/config/app/bankFinanceChannel/XS10115/config.properties","APt9sIBeBywe/9eGqIMgNy+TJfVzFvCeKgd51KDVZy8C");
//        Method send = aClass.getMethod("send", new Class[]{String.class, String.class,String.class});
//        send.invoke(null,"12","1","2");
        System.out.println(aClass.getClassLoader());*/
    }
}
