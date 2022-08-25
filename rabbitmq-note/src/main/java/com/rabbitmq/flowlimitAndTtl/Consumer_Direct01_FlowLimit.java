package com.rabbitmq.flowlimitAndTtl;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费端限流：
 *    RabbitMq 提供了一种qos(服务质量保证) 功能， 即在非自动确认消息的前提下，
 *    如果一定数目的消息（通过基于consume或者channel设置Qos 的值）未被确认前，不进行消费新的消息
 * 使用条件：
 *  1.开启手动签收
 *  2.void basicQos(int prefetchSize, int prefetchCount, boolean global)
 */
public class Consumer_Direct01_FlowLimit {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionConfig.getConnection();
        String exchangeName = "test_direct";
        Channel channel = connection.createChannel();
        String queueName = "test_direct_queue1";
        String routingKey = "direct01";
        channel.queueBind(queueName,exchangeName,routingKey);
        /**
         * void basicQos(int prefetchSize, int prefetchCount, boolean global)
         * @param prefetchSize 消费端 ： 0，不限制
         * @param prefetchCount 一次最多处理多少条消息，一般在工作中设置为1 就好
         * @param global 这个限流策略是在什么上应用的，RabbitMq 上有两个级别， 1.channel, 2,consumer, 一般是false, true:在 channel， false：在consumer
         */
        channel.basicQos(0,1,false);
        // 接收消息
        channel.basicConsume(queueName,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //System.out.println("Exchange："+envelope.getExchange());
                //System.out.println("RoutingKey："+envelope.getRoutingKey());
                System.out.println("body："+new String(body));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答】
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
