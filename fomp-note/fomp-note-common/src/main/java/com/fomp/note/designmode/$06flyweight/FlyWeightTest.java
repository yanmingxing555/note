package com.fomp.note.designmode.$06flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 享元模式：
 *      模式定义: 运用共享技术有效地支持大量细粒度的对象
 *      优点： 如果系统有大量类似的对象，可以节省大量的内存及CPU资源
 *      JDK源码中的应用 :
 *          1 String,Integer,Long...
 *          2 com.sun.org.apache.bcel.internal.generic.InstructionConstants
 */
public class FlyWeightTest {
    public static void main(String[] args) {
        TreeNode treeNode1=new TreeNode( 3,4, TreeFactory.getTree("xxx","xxxxxxx xx" ));
        TreeNode treeNode2=new TreeNode( 5,4, TreeFactory.getTree("xxx","xxxxxxx xx" ));
        TreeNode treeNode3=new TreeNode( 13,24, TreeFactory.getTree("yyy","xxxxx xxxx" ));
        TreeNode treeNode4=new TreeNode( 15,24, TreeFactory.getTree("yyy","xxxxx xxxx" ));
    }
}

class TreeNode{
    private int x;
    private int y;
    private Tree tree;
    public TreeNode(int x, int y, Tree tree) {
        this.x = x;
        this.y = y;
        this.tree = tree;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Tree getTree() {
        return tree;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
class TreeFactory{
    private static Map<String,Tree> map=new ConcurrentHashMap<>();
    public static Tree getTree(String name,String data){
        if (map.containsKey( name )){
            return map.get(name);
        }
        Tree tree=new Tree(name,data);
        map.put(name,tree);
        return tree;
    }
}
class Tree{
    private final String name;
    private final String data;
    public Tree(String name, String data) {
        System.out.println(" name: "+name +" tree created. ");
        this.name = name;
        this.data = data;
    }
    public String getName() {
        return name;
    }
    public String getData() {
        return data;
    }
}