package com.rabbitmq.flowlimitAndTtl;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Producer_Direct_FlowLimit_TTL {
    public static void main(String[] args) throws Exception {
        flow_limit();
    }
    /**
     *
     */
    private static void flow_limit() throws Exception {
        //3. 创建连接 Connection
        Connection connection = ConnectionConfig.getConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        String exchangeName = "test_direct";
        //5. 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,false,null);
        //6. 创建队列
        String queueName = "test_direct_queue";
        String routingKey = "direct01";
        /**
         * 指定队列参数TTL 全称 Time To Live（存活时间/过期时间）
         * ➢ 设置队列过期时间使用参数：x-message-ttl，单位：ms(毫秒)，会对整个队列消息统一过期。
         * ➢ 设置消息过期时间使用参数：expiration。单位：ms(毫秒)，当该消息在队列头部时（消费时），会单独判断
         *      这一消息是否过期。
         * ➢ 如果两者都进行了设置，以时间短的为准
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",10*1000);//TTL--指定队列过期时间

        channel.queueDeclare(queueName,true,false,false,arguments);
        //7. 绑定队列和交换机
        channel.queueBind(queueName,exchangeName,routingKey);
        String body1 = "发送信息：routingKey = "+routingKey;
        //8. 发送消息
        for (int i = 0; i < 10; i++) {
            AMQP.BasicProperties props = new AMQP.BasicProperties()
                    .builder()
                    .contentEncoding("GBK")//指定消息的字符集
                    //.contentType("")//contentType类型
                    .expiration("5000")//指定该消息的过期时间
                    .build();
            channel.basicPublish(exchangeName,routingKey,props,body1.getBytes());
        }
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
