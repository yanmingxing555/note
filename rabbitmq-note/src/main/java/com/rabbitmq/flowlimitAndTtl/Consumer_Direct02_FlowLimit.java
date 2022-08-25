package com.rabbitmq.flowlimitAndTtl;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer_Direct02_FlowLimit {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionConfig.getConnection();
        String exchangeName = "test_direct";
        Channel channel = connection.createChannel();
        String queueName = "test_direct_queue2";
        String routingKey = "direct02";
        channel.queueBind(queueName,exchangeName,routingKey);
        // 接收消息
        channel.basicConsume(queueName,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Exchange："+envelope.getExchange());
                System.out.println("RoutingKey："+envelope.getRoutingKey());
                System.out.println("body："+new String(body));
                //手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答】
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
