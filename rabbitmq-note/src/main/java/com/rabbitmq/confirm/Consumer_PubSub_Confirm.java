package com.rabbitmq.confirm;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer_PubSub_Confirm {

    public static void main(String[] args) throws Exception {
        //3. 创建连接 Connection
        Connection connection = ConnectionConfig.getConnection();
        String exchangeName = "test_fanout";
        //4. 创建Channel
        Channel channel = connection.createChannel();
        String queue1Name = "test_direct_queue1";
        String queue2Name = "test_direct_queue2";

        /**
         * 5.0版本以后有
         */
        /*DeliverCallback deliverCallback =  (consumerTag, delivery) ->{
            String message = new String(delivery.getBody(), "UTF-8");
        };*/
        // 接收消息
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              /*  System.out.println("consumerTag："+consumerTag);
                System.out.println("Exchange："+envelope.getExchange());
                System.out.println("RoutingKey："+envelope.getRoutingKey());
                System.out.println("properties："+properties);*/
                System.out.println("body："+new String(body));
                System.out.println("将日志信息打印到控制台.....");
                /**
                 * //确认消息
                 * channel.basicAck(deliveryTag, multiple);
                 * //拒绝消息
                 * channel.basicNack(deliveryTag, multiple, requeue);
                 * //拒绝消息
                 * channel.basicReject(deliveryTag, requeue)
                 * //扔掉消息
                 * channel.BasicReject(result.DeliveryTag, false);
                 * //退回消息
                 * channel.BasicReject(result.DeliveryTag, true);
                 * //批量退回或删除,中间的参数 是否批量 true是/false否 (也就是只一条)
                 * channel.BasicNack(result.DeliveryTag, true, true);
                 * //补发消息 true退回到queue中/false只补发给当前的consumer；BasicRecover方法则是进行补发操作，
                 * //其中的参数如果为true是把消息退回到queue但是有可能被其它的consumer接收到，设置为false是只补发给当前的consumer
                 * channel.BasicRecover(true);
                 */
                //手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答】
                channel.basicAck(envelope.getDeliveryTag(), false);
                //channel.basicNack(envelope.getDeliveryTag(), false, false);
            }
        };
        channel.basicConsume(queue1Name,false,consumer);
        channel.basicConsume(queue2Name,false,consumer);
        //关闭资源？不要

    }
}
