package com.fomp.note.designmode.$02factory;

class Factory {
    /**
     *工厂模式
     * 模式定义: 定义一个用于创建对象的接口，让子类决定实例化哪一个类。
     *  Factory Method 使得一个类的实例化延迟到子类
     */
    /**
     * 应用场景：
     *      1.当你不知道改使用对象的确切类型的时候
     *      2.当你希望为库或框架提供扩展其内部组件的方法时
     * 主要优点：
     *      1.将具体产品和创建者解耦
     *      2.符合单一职责原则
     *      3.符合开闭原则
     *  源码中的应用：
     *      1 java api
     *      2 静态工厂方法 3
     *      4 Calendar.getInstance()
     *      5 java.text.NumberFormat.getInstance()
     *      6 java.util.ResourceBundle.getBundle()
     *      7 工厂方法
     *      8 java.net.URLStreamHandlerFactory
     *      9 javax.xml.bind.JAXBContext.createMarshaller
     */
}

/**
 * 简单工厂
 */
class SimpleFactory {
    public static ProductInter createProductInter(String type) {
        if (type.equals("0")) {
            return new ProductA();
        } else if (type.equals("1")) {
            return new ProductA1();
        } else {
            return null;
        }
    }
}

/**
 * 工厂方法
 */
//稳定接口
interface ProductInter {
    public void method1();
}
//具体实现
class ProductA implements ProductInter {
    public void method1() {
        System.out.println( "ProductA.method1 executed. " );
    }
}
class ProductA1 implements ProductInter {
    public void method1() {
        System.out.println( "ProductA1.method1 executed. " );
    }
}
abstract class Application {
    abstract ProductInter createProduct();
    ProductInter getObject() {
        ProductInter product = createProduct();
        return product;
    }
}
//工厂方法具体实现类
class ConcreteProductA extends Application {
    @Override
    ProductInter createProduct() {
        return new ProductA();
    }
}
class ConcreteProductA1 extends Application {
    @Override
    ProductInter createProduct() {
        return new ProductA1();
    }
}
