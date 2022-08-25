package com.fomp.note.designmode.$12observer;

/**
 * 观察者模式：监听Listener
 *
 * 模式定义： 定义了对象之间的一对多依赖，让多个观察者对象同时监听某一个主题对象，
 *          当主题对象发生变化时，它的所有依赖者都会收到通知并更新
 *
 * 应用场景：当更改一个对象的状态可能需要更改其他对象，
 *          并且实际的对象集事先 未知或动态更改时，请使用观察者模式。
 * 优点： 1.符合开闭原则
 *      2.可以在运行时建立对象之间的关系
 * JDK&Spring源码中的应用
 *      1 JDK:
 *      2 java.util.Observable
 *      3 Spring:
 *      4 org.springframework.context.ApplicationListener
 *
 */
public class Observer {
}
