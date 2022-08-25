package com.fomp.note.designmode.$04builder;

/**
 *建造者模式
 * 模式定义: 将一个复杂对象的创建与他的表示分离，使得同样的构建过程可以创建不同的表示
 * 应用场景：1.需要生成的对象具有复杂的内部结构
 *         2.需要生成的对象内部属性本身相互依赖
 *         3.与不可变对象配合使用
 *  优点：1、建造者独立，易扩展。
 *       2、便于控制细节风险。
 *  Spring源码中的应用：
 *      1 org.springframework.web.servlet.mvc.method.RequestMappingInfo
 *      2 org.springframework.beans.factory.support.BeanDefinitionBuilder
 *
 */
public class BuilderTest {
    public static void main(String[] args) {
        ProductBuilder specialConcreteProductBuilder=new SpecialConcreteProductBuilder();
        Director director=new Director(specialConcreteProductBuilder);
        Product product=director.makeProduct( "productNamexxx", "cn...", "part1", "part2", "part3", "part4" );
        System.out.println(product);
    }
}
interface ProductBuilder{
    void builderProductName(String productName);
    void builderCompanyName(String companyName);
    void builderPart1(String part1);
    void builderPart2(String part2);
    void builderPart3(String part3);
    void builderPart4(String part4);
    Product build();
}
class DefaultConcreteProductBuilder implements ProductBuilder{
    private String productName;
    private String companyName;
    private String part1;
    private String part2;
    private String part3;
    private String part4;
    @Override
    public void builderProductName(String productName) {
        this.productName=productName;
    }
    @Override
    public void builderCompanyName(String companyName) {
        this.companyName=companyName;
    }
    @Override
    public void builderPart1(String part1) {
        this.part1 = part1;
    }
    @Override
    public void builderPart2(String part2) {
        this.part2 = part2;
    }
    @Override
    public void builderPart3(String part3) {
        this.part3 = part3;
    }
    @Override
    public void builderPart4(String part4) {
        this.part4 = part4;
    }
    @Override
    public Product build() {
        return new Product(this.productName,this.companyName,this.part1,this.part2,this.part3,this.part4 );
    }
}
class SpecialConcreteProductBuilder implements ProductBuilder{
    private String productName;
    private String companyName;
    private String part1;
    private String part2;
    private String part3;
    private String part4;
    @Override
    public void builderProductName(String productName) {
        this.productName=productName;
    }
    @Override
    public void builderCompanyName(String companyName) {
        this.companyName=companyName;
    }
    @Override
    public void builderPart1(String part1) {
        this.part1 = part1;
    }
    @Override
    public void builderPart2(String part2) {
        this.part2 = part2;
    }
    @Override
    public void builderPart3(String part3) {
        this.part3 = part3;
    }
    @Override
    public void builderPart4(String part4) {
        this.part4 = part4;
    }
    @Override
    public Product build() {
        return new Product(this.productName,this.companyName,this.part1,this.part2,this.part3,this.part4 );
    }
}
class Director{
    private ProductBuilder builder;
    public Director(ProductBuilder builder) {
        this.builder=builder;
    }
    public Product makeProduct(String productName, String companyName, String part1, String part2, String part3, String part4){
        builder.builderProductName(productName );
        builder.builderCompanyName(companyName );
        builder.builderPart1(part1 );
        builder.builderPart2(part2 );
        builder.builderPart3(part3 );
        builder.builderPart4(part4 );
        Product product=builder.build();
        return product;
    }
}
class Product{
    private String productName;
    private String companyName;
    private String part1;
    private String part2;
    private String part3;
    private String part4;

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", part1='" + part1 + '\'' +
                ", part2='" + part2 + '\'' +
                ", part3='" + part3 + '\'' +
                ", part4='" + part4 + '\'' +
                '}';
    }
    public Product(String productName, String companyName, String part1, String part2, String part3, String part4) {
        this.productName = productName;
        this.companyName = companyName;
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
        this.part4 = part4;
    }
    public Product() {
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setPart1(String part1) {
        this.part1 = part1;
    }
    public void setPart2(String part2) {
        this.part2 = part2;
    }
    public void setPart3(String part3) {
        this.part3 = part3;
    }
    public void setPart4(String part4) {
        this.part4 = part4;
    }
    public String getProductName() {
        return productName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getPart1() {
        return part1;
    }
    public String getPart2() {
        return part2;
    }
    public String getPart3() {
        return part3;
    }
    public String getPart4() {
        return part4;
    }
}

