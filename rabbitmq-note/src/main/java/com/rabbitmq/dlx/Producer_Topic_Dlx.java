package com.rabbitmq.dlx;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer_Topic_Dlx {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionConfig.getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_dlx_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);
        String queueName = "test_dlx_queue";
        String routingKey = "dlx.save";
        channel.queueBind(queueName,exchangeName,routingKey);
        String body1 = "发送信息：routingKey = "+routingKey;
        //8. 发送消息
        for (int i = 0; i < 10; i++) {
            AMQP.BasicProperties props = new AMQP.BasicProperties()
                    .builder()
                    .contentEncoding("GBK")//指定消息的字符集
                    //.contentType("text/plain")//contentType类型
                    //.contentType("application/json")//contentType类型
                    .expiration("10000")//指定该消息的过期时间
                    .build();
            channel.basicPublish(exchangeName,routingKey,props,body1.getBytes());
        }
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
