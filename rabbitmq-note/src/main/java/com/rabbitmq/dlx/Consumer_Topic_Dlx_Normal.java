package com.rabbitmq.dlx;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. 死信交换机和死信队列和普通的没有区别
 * 2. 当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列
 * 3. 消息成为死信的三种情况：
 *    ①队列消息长度到达限制；
 *    ②消费者拒接消费消息，并且不重回队列；消息被拒绝（basic.reject/ basic.nack） 并且 requeue=false
 *    ③原队列存在消息过期设置，消息到达超时时间未被消费；
 */
public class Consumer_Topic_Dlx_Normal {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionConfig.getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);
        /**
         * 给队列设置参数： x-dead-letter-exchange 和 x-dead-letter-routing-key
         */
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange","dlx.exchange");
        arguments.put("x-dead-letter-routing-key","dlx.routing.key.dead");
        channel.queueDeclare(queueName, true, false, false, arguments);
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
