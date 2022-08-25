package com.rabbitmq.confirm;

import com.rabbitmq.ConnectionConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * RabbitMQ消息可靠性：
 * 1、生产者事务模式
 * 2、生产者Confirm模式
 * 3、消费者的签收机制：
 *      • 自动确认：acknowledge="none" • 手动确认：acknowledge="manual"
 *      • 根据异常情况确认：acknowledge="auto"
 *      • 注意：事务方式和Confirm两种模式不能共存
 */
/**
 * ➢ 设置ConnectionFactory的publisher-confirms="true" 开启 确认模式。
 * ➢ 使用rabbitTemplate.setConfirmCallback设置回调函数。当消息发送到exchange后回
 * 调confirm方法。在方法中判断ack，如果为true，则发送成功，如果为false，则发
 * 送失败，需要处理。
 * ➢ 设置ConnectionFactory的publisher-returns="true" 开启 退回模式。
 * ➢ 使用rabbitTemplate.setReturnCallback设置退回函数，当消息从exchange路由到
 * queue失败后，如果设置了rabbitTemplate.setMandatory(true)参数，则会将消息退
 * 回给producer。并执行回调函数returnedMessage。 ➢ 在RabbitMQ中也提供了事务机制，但是性能较差，此处不做讲解。
 * 使用channel下列方法，完成事务控制：
 * txSelect(), 用于将当前channel设置成transaction模式
 * txCommit()，用于提交事务
 * txRollback(),用于回滚事务
 */
public class Producer_PubSub_Confirm {
    public static void main(String[] args) throws Exception {
        confirm03();
    }
    /**
     * 异步 confirm 模式：
     *   Channel 对象提供的 ConfirmListener()回调方法只包含 deliveryTag（当前 Chanel 发出的消息序号），
     *  我们需要自己为每一个 Channel 维护一个 unconfirm 的消息序号集合，每 publish 一条数据，集合中元素加 一，每回调一次 handleAck方法，
     *  unconfirm 集合删掉相应的一条（multiple=false）或多条（multiple=true）记录。从程序运行效率上看，这个unconfirm 集合最好采用有序集合 SortedSet 存储结构。
     *  实际上，SDK 中的 waitForConfirms()方法也是通过 SortedSet维护消息序号的。
     */
    private static void confirm03() throws Exception {
        //3. 创建连接 Connection
        Connection connection = ConnectionConfig.getConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        String exchangeName = "test_direct";
        //5. 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,false,null);
        //6. 创建队列
        String queue1Name = "test_direct_queue1";
        String queue2Name = "test_direct_queue2";
        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7. 绑定队列和交换机
        channel.queueBind(queue1Name,exchangeName,"test_direct");
        channel.queueBind(queue2Name,exchangeName,"test_direct");
        String body = "日志信息：张三调用了findAll方法...日志级别：info...";

        /***将channel设置为confirm模式***/
        channel.confirmSelect();
        /***创建一个列表，用于维护消息发送的情况***/
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //8. 发送消息
        /*while (true) {}*/
        // Confirm模式下，返回下一条要发布的消息的序列号。
        long nextSeqNo = channel.getNextPublishSeqNo();
        //Thread.sleep(2000);
        //channel.basicPublish(exchangeName,"",null,body.getBytes());
        /**
         * @param mandatory 如果为true, 则监听器会接收到路由不可达的消息，然后进行后续处理， 如果为false, 那么broker端自动删除该消息；
         */
        channel.basicPublish(exchangeName,"test_direct",true,null,body.getBytes());
        confirmSet.add(nextSeqNo);

        //confirm03_Callback(channel,confirmSet);
        confirm03_Listener(channel,confirmSet);

        //等待
        Thread.sleep(5 * 1000L);
        //9. 释放资源
        channel.close();
        connection.close();
    }
    /**
     * rabbitmq 整个消息投递的路径为：
     * producer--->rabbitmq broker--->exchange--->queue--->consumer
     *  ⚫ 消息从 producer 到 exchange 则会返回一个 confirmCallback 。
     *  ⚫ 消息从 exchange-->queue 投递失败则会返回一个 returnCallback 。
     */
    private static void confirm03_Listener(final Channel channel,final SortedSet<Long> confirmSet){
        // 添加Confirm监听器，监听消息发送的状态。==>生产者queue
        channel.addConfirmListener(new ConfirmListener() {
            //每回调一次handleAck方法，confirmSet 删掉相应的一条（multiple=false）或多条（multiple=true）记录。
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ack: deliveryTag = "+deliveryTag+" multiple: "+multiple);
                if (multiple) {
                    System.out.println("移除集合中的多条记录--");
                    confirmSet.headSet(deliveryTag + 1).clear(); //用一个SortedSet, 返回此有序集合中小于end的所有元素。
                } else {
                    System.out.println("--multiple false--");
                    confirmSet.remove(deliveryTag);
                }
            }
            //消息签收失败时调用
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }
        });
        channel.addReturnListener(new ReturnListener() {
            /**
             * @param replyCode - 错误码
             * @param replyText - 错误信息
             * @param exchange - 交换机
             * @param routingKey - 路由键
             * @param properties - 参数信息
             * @param body - 消息
             */
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode："+replyCode);
                System.out.println("replyText："+replyText);
                System.out.println("exchange："+exchange);
                System.out.println("routingKey："+routingKey);
                System.out.println("properties："+properties.toString());
                System.out.println("body："+new String(body));
            }
        });
    }
    /**
     * 普通 confirm 模式：每发送一条消息后，调用 waitForConfirms()方法，等待服务器端confirm。实际上是一种串行 confirm 了
     */
    private static void confirm01() throws Exception {
        //3. 创建连接 Connection
        Connection connection = ConnectionConfig.getConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        String exchangeName = "test_fanout";
        //5. 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,false,null);
        //6. 创建队列
        String queue1Name = "test_fanout_queue1";
        String queue2Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7. 绑定队列和交换机
        channel.queueBind(queue1Name,exchangeName,"");
        channel.queueBind(queue2Name,exchangeName,"");
        String body = "日志信息：张三调用了findAll方法...日志级别：info...";

        /***将channel设置为confirm模式***/
        channel.confirmSelect();
        //8. 发送消息
        channel.basicPublish(exchangeName,"",null,body.getBytes());

        /***判断消息是否发送成功：超时抛出异常***/
        if (channel.waitForConfirms(30*1000)) {
            System.out.println("已发送消息：" + body);
        } else {
            System.out.println("消息发送失败，消息内容 ：" + body);
        }
        //9. 释放资源
        channel.close();
        connection.close();
    }
    /**
     * 批量 confirm 模式：客户端程序需要定期（每隔多少秒）或者定量（达到多少条）或者两则结合起来publish 消息，然后等待服务器端 confirm, 相比普通 confirm 模式，批量极大提升 confirm 效率，但是问题在于一旦出现 confirm 返回 false 或者超时的情况时，客户端需要将这一批次的消息全部重发，这会带来明显的重复消息数量，并且，当消息经常丢失时，批量 confirm 性能应该是不升反降的。
     */
    private static void confirm02() throws Exception {
        //3. 创建连接 Connection
        Connection connection = ConnectionConfig.getConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        String exchangeName = "test_fanout";
        //5. 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,false,null);
        //6. 创建队列
        String queue1Name = "test_fanout_queue1";
        String queue2Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7. 绑定队列和交换机
        channel.queueBind(queue1Name,exchangeName,"");
        channel.queueBind(queue2Name,exchangeName,"");
        String body = "日志信息：张三调用了findAll方法...日志级别：info...";

        /***将channel设置为confirm模式***/
        channel.confirmSelect();
        //8. 发送消息
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(exchangeName,"",null,body.getBytes());
        }
        /***判断消息是否发送成功***/
        //channel.waitForConfirmsOrDie();
        if (channel.waitForConfirms()) {
            System.out.println("已发送消息：" + body);
        } else {
            System.out.println("消息发送失败，消息内容 ：" + body);
        }
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
