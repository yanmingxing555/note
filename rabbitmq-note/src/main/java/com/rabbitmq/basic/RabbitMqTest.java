package com.rabbitmq.basic;

public class RabbitMqTest {
    /**
     * MQ全称 Message Queue（消息队列），是在消息的传输过程中保存消息的容器。多用于分布式系统之间进行通信。
     *  ⚫ MQ，消息队列，存储消息的中间件
     *  ⚫ 分布式系统通信两种方式：直接远程调用 和 借助第三方 完成间接通信
     *  ⚫ 发送方称为生产者，接收方称为消费者
     * MQ优势：
     *  ⚫ 应用解耦：提高系统容错性和可维护性
     *  ⚫ 异步提速：提升用户体验和系统吞吐量
     *  ⚫ 削峰填谷：提高系统稳定性
     * MQ 有优势也有劣势，那么使用 MQ 需要满足什么条件呢？
     *  ① 生产者不需要从消费者处获得反馈。引入消息队列之前的直接调用，其接口的返回值应该为空，这才让明
     *    明下层的动作还没做，上层却当成动作做完了继续往后走，即所谓异步成为了可能。
     *  ② 容许短暂的不一致性。
     *  ③ 确实是用了有效果。即解耦、提速、削峰这些方面的收益，超过加入MQ，管理MQ这些成本。
     */
    /**
     * RabbitMQ 中的相关概念：
     * ⚫ Broker：接收和分发消息的应用，RabbitMQ Server就是 Message Broker
     * ⚫ Virtual host：出于多租户和安全因素设计的，把 AMQP 的基本组件划分到一个虚拟的分组中，类似于网
     *    络中的 namespace 概念。当多个不同的用户使用同一个 RabbitMQ server 提供的服务时，可以划分出多
     *    个vhost，每个用户在自己的 vhost 创建 exchange／queue 等
     * ⚫ Connection：publisher／consumer 和 broker 之间的 TCP 连接
     * ⚫ Channel：如果每一次访问 RabbitMQ 都建立一个 Connection，在消息量大的时候建立 TCP Connection
     *    的开销将是巨大的，效率也较低。Channel 是在 connection 内部建立的逻辑连接，如果应用程序支持多线
     *    程，通常每个thread创建单独的 channel 进行通讯，AMQP method 包含了channel id 帮助客户端和
     *    message broker 识别 channel，所以 channel 之间是完全隔离的。Channel 作为轻量级的 Connection
     *    极大减少了操作系统建立 TCP connection 的开销
     * ⚫ Exchange：message 到达 broker 的第一站，根据分发规则，匹配查询表中的 routing key，分发消息到
     *    queue 中去。常用的类型有：direct (point-to-point), topic (publish-subscribe) and fanout (multicast)
     * ⚫ Queue：消息最终被送到这里等待 consumer 取走
     * ⚫ Binding：exchange 和 queue 之间的虚拟连接，binding 中可以包含 routing key。Binding 信息被保存
     *    到 exchange 中的查询表中，用于 message 的分发依据
     */
    /**
     * RabbitMQ 提供了 6 种工作模式：
     *      简单模式、
     *      work queues、
     *      Publish/Subscribe 发布与订阅模式、
     *      Routing路由模式、
     *      Topics 主题模式、
     *      RPC 远程调用模式
     *
     * 注意：
     *  使用rabbitmq时，如果在同一个topic交换机下绑定了不同的routingkey A和B，指向同一个queue。
     *  那么消费者在监听queue队列时，A和B的消息都会被消费，即使在监听时指定了不同的key。
     *  一旦queue在broker中声明过，那么消费者监听的就是queue，与key无关
     */
}
