package com.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: ymx
 * @date: 2022/7/18
 * @version: 1.0.0.0
 */
public class ConnectionConfig {
    public static Connection getConnection() throws Exception{
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址：默认localhost
        factory.setHost("localhost");
        //端口：默认：5672
        factory.setPort(5672);
        //用户名，默认：guest
        factory.setUsername("third_test");
        //密码，默认：guest
        factory.setPassword("third_test");
        //虚拟机地址，默认：/
        factory.setVirtualHost("/third_test");
        //创建连接
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
