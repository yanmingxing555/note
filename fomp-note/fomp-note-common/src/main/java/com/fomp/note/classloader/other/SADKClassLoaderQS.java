package com.fomp.note.classloader.other;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

/**
 * 针对不同银行加载不同sadk
 */
public class SADKClassLoaderQS  extends URLClassLoader {

    public final static String jar_ZSB_Path = "sadk/SADK3631Patch-3.0.2.0.jar";
    public final static String jar_PAB_Path = "sadk/SADK-3.2.1.3.jar";
    private static volatile SADKClassLoaderQS instance;
    private static URL url;

    public synchronized static SADKClassLoaderQS getInstance(URL[] urls, ClassLoader parent){
        if(instance == null){
            instance = new SADKClassLoaderQS(urls, parent);
        }
        url = urls[0];
        return instance;
    }

    private SADKClassLoaderQS(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.indexOf("crypto") >= 1) {
            return findClass(name);
        }
        Class<?> c = super.findLoadedClass(name);
        if(c != null){
            return c;
        }
        return super.loadClass(name);
    }
    private String getPath()throws Exception{
        return URLDecoder.decode(this.getClass().getResource("/").getPath(),"UTF-8").split("classes")[0];
    }
    /**
     * 根据银行编码加载不同的jar
     */
    /*public void loadJarByBankCode(String bankCode){
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            URL[] urls = null;
            if ("ZSB".equals(bankCode)) {
                urls = new URL[]{new File(getPath().substring(1)+SADKClassLoaderQS.jar_ZSB_Path).toURL()};
            }else{
                urls = new URL[]{new File(getPath().substring(1)+SADKClassLoaderQS.jar_PAB_Path).toURL()};
            }
            SADKClassLoaderQS classLoader = SADKClassLoaderQS.getInstance(urls, oldClassLoader);
            Thread.currentThread().setContextClassLoader(classLoader);
            Class<?> calss_JCrypto = classLoader.loadClass("cfca.sadk.lib.crypto.JCrypto");
            String jarName = calss_JCrypto.getProtectionDomain().getCodeSource().getLocation().getFile();
            log.info("加载jar【"+jarName+"】成功！");
        } catch (Exception e) {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
            log.error("自定义类加载器出错："+e.getMessage(),e);
        }
    }

    public void loadJarByBankCode2(String bankCode) throws Exception{
        final String jar_ZSB_Path = "sadk/SADK3631Patch-3.0.2.0.jar";
        final String jar_PAB_Path = "sadk/SADK-3.2.1.3.jar";
        Method method = null;
        boolean accessible = false;
        boolean iaAccessible = false;
        try {
            String path = getPath().substring(1);
            String jarPath = "";
            if ("ZSB".equals(bankCode)) {
                jarPath = path + jar_ZSB_Path;
            }else{
                jarPath = path + jar_PAB_Path;
            }
            File jarFile = new File(jarPath);
            //从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            //获取方法的访问权限以便写回
            accessible = method.isAccessible();
            iaAccessible = true;
            method.setAccessible(true);
            //获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = jarFile.toURI().toURL();
            method.invoke(classLoader, url);
            Class<?> calss_JCrypto = classLoader.loadClass("cfca.sadk.lib.crypto.JCrypto");
            String jarName = calss_JCrypto.getProtectionDomain().getCodeSource().getLocation().getFile();
            log.info("加载jar【"+jarName+"】成功！");
        } catch (Exception e) {
            log.error("自定义类加载器出错："+e.getMessage(),e);
        } finally {
            if (method!=null && iaAccessible) {
                method.setAccessible(accessible);
            }
        }
    }*/
}
