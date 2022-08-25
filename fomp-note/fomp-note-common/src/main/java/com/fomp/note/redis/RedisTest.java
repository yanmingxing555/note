package com.fomp.note.redis;

public class RedisTest {
    public static void main(String[] args) {

        /********************redis命令********************/
        /**
          key值的操作：
                  keys  * 查看当前库所有key
                  set key value 设置key值与value
                  exists key 判断key是否存在
                  type key 查看key是什么类型
                  del key 删除指定的key数据
                  unlink key 根据value选择非阻塞删除------仅将keys从keyspace元数据中删除，真正的删除会在后续异步操作。
                  expire key 10 10秒钟：为给定的key设置过期时间
                  ttl key 查看还有多少秒过期，-1表示永不过期，-2表示已过期
          库的选择：
                  select 命令切换数据库
                  dbsize 查看当前数据库的key数量
                  flushdb 清空当前库
                  flushall 通杀全部库
         string字符串：
                set key value 设置key值
                get key 查询key值
                append key value 将给定的value追加到原值末尾
                strlen key 获取值的长度
                setnx key value 只有在key不存在的时候，设置key值
                incr key 将key值存储的数字增1，只对数字值操作，如果为空，新增值为1
                decr key 将key值存储的数字减1，只对数字值操作，如果为空，新增值为1
                decr key 将key值存储的数字减1
                incrby/decrby key <步长> 将key值存储的数字增减如步长
         补充额外的字符串参数：
                 mset key value key value..同时设置一个或者多个key-value
                 mget key key...同时获取一个或多个value
                 msetnx key value key value..同时设置一个或者多个key-value.当且仅当所有给定key都不存在
                 getrange key <起始位置> <结束位置> 获取key的起始位置和结束位置的值
                 setrange key <起始位置> value 将value的值覆盖起始位置开始
                 setex key <> value 设置键值的同时,设置过期时间
                 getset key value 用新值换旧值
         list列表常用命令:
                 lpush/rpush key value value...从左或者右插入一个或者多个值(头插与尾插)
                 lpop/rpop key 从左或者右吐出一个或者多个值(值在键在,值都没,键都没)
                 rpoplpush key1 key2 从key1列表右边吐出一个值,插入到key2的左边
                 lrange key start stop 按照索引下标获取元素(从左到右)
                 lrange key 0 -1 获取所有值
                 lindex key index 按照索引下标获得元素
                 llen key 获取列表长度
                 linsert key before/after value newvalue 在value的前面插入一个新值
                 lrem key n value 从左边删除n个value值
         set集合  字典，哈希表  自动排重且为无序的
            常用命令:
                 sadd key value value... 将一个或者多个member元素加入集合key中,已经存在的member元素被忽略
                 smembers key 取出该集合的所有值
                 sismember key value 判断该集合key是否含有改值
                 scard key 返回该集合的元素个数
                 srem key value value 删除集合中的某个元素
                 spop key 随机从集合中取出一个元素
                 srandmember key n 随即从该集合中取出n个值，不会从集合中删除
                 smove <一个集合a><一个集合b>value 将一个集合a的某个value移动到另一个集合b
                 sinter key1 key2 返回两个集合的交集元素
                 sunion key1 key2 返回两个集合的并集元素
                 sdiff key1 key2 返回两个集合的差集元素（key1有的，key2没有）
         hash哈希
             键值对集合，特别适合用于存储对象类型
             常用命令：
                 hset key field value 给key集合中的filed键赋值value
                 hget key1 field 集合field取出value
                 hmset key1 field1 value1 field2 value2 批量设置hash的值
                 hexists key1 field 查看哈希表key中，给定域field是否存在
                 hkeys key 列出该hash集合的所有field
                 hvals key 列出该hash集合的所有value
                 hincrby key field increment 为哈希表key中的域field的值加上增量1 -1
                 hsetnx key field value 将哈希表key中的域field的值设置为value，当且仅当域field不存在
         zset有序集合
             没有重复元素的字符串集合，按照相关的分数进行排名，排名从低到高，排名可重复
             有序集合 zset 与普通集合 set 非常相似，是一个没有重复元素的字符串集合
             常用命令：
                 zadd key score1 value1 score2 value2 将一个或多个member元素及其score值加入到有序key中
                 zrange key start stop (withscores) 返回有序集key，下标在start与stop之间的元素，带withscores，可以让分数一起和值返回到结果集。
                 zrangebyscore key min max(withscores) 返回有序集key，所有score值介于min和max之间（包括等于min或max）的成员。有序集成员按score的值递增次序排列
                 zrevrangebyscore key max min （withscores）同上，改为从大到小排列
                 zincrby key increment value 为元素的score加上增量
                 zrem key value 删除该集合下，指定值的元素
                 zcount key min max 统计该集合，分数区间内的元素个数
                 zrank key value 返回该值在集合中的排名，从0开始
         */

    }
    /**
     * 一、Redis 是一个基于内存的高性能 key-value 数据库。是完全开源免费的，用C语言编写的，遵守BSD协议。
     * 二、Redis是单线程+多路IO复用技术：
     *      多路复用是指使用一个线程来检查多个文件描述符（Socket）的就绪状态，比如调用select和poll函数，传入多个文件描述符，
     *      如果有一个文件描述符就绪，则返回，否则阻塞直到超时。得到就绪状态后进行真正的操作可以在同一个线程里执行，也可以启动线程执行（比如使用线程池）
     * 三、Redis特点：
     *      1.高效性：Redis是基于内存操作的，吞吐量可以在 1s内完成十万次读写操作
     *      2.原子性：Redis的读写模块是单线程，每个操作都具原子性
     *      3.支持多种数据结构：
 *             ①string（字符串）：
 *                  Redis 所有的数据结构都是以唯一的 key 字符串作为名称，然后通过这个唯一 key 值来获取相应的 value 数据。
     *              String 采用预分配冗余空间的方式来减少内存的频繁分配，内部为当前字符串实际分配的空间 capacity 一般要高于实际字符串长度 len。
     *             当字符串长度小于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时一次只会多扩 1M 的空间。需要注意的是字符串最大长度为 512M。
 *             ②list（列表,相当于LinkedList）：
     *              Redis 的列表相当于 Java 语言里面的 LinkedList，注意它是链表而不是数组，而且是双向链表。这意味着 list 的插入和删除操作非常快，但是索引定位很慢
 *             ③hash（哈希字典,相当于 Java 语言里面的 HashMap，它是无序字典）：
     *             hash的底层存储有两种数据结构：
     *                  ziplist：如果hash对象保存的键和值字符串长度都小于64字节且hash对象保存的键值对数量小于512，则采用这种
     *                  dict（字典）：其他情况采用这种数据结构
     *             hash 结构也可以用来存储用户信息，不同于字符串一次性需要全部序列化整个对象，hash 以对用户结构中的每个字段单独存储。
     *                  这样当我们需要获取用户信息时可以进行部分获取。而以整个符串的形式去保存用户信息的话就只能一次性全部读取，这样就会比较浪费网络流量。
     *             hash 也有缺点，hash 结构的存储消耗要高于单个字符串，到底该使用 hash 还是字符串，需要根据实际情况再三权衡。
 *             ④set（集合：无序的、自动去重）：
     *                  intset：如果元素个数少于默认值512且元素可以用整型，则用这种数据结构
     *                  dict（字典）：其他情况采用这种数据结构
 *             ⑤zset(有序集合)：
     *                 ziplist ：如果有序集合保存的所有元素的长度小于默认值64字节且有序集合保存的元素数量小于默认值128个，则采用这种数据结构
     *                 字典（dict） + 跳表（skiplist）：其他情况采用这种数据结构
     *      4.稳定性：持久化，主从复制（集群）
     *      5.其他特性：支持过期时间，支持事务，消息订阅。
     * 四、Redis适用场景：
     *      1.配合关系型数据库做高速缓存，减少数据库IO，例如：高频次，热门访问的数据；session共享数据
     *      2.由于其拥有持久化能力,利用其多样的数据结构存储特定的数据
     * 五、Redis过期策略：Redis 所有的数据结构都可以设置过期时间，时间一到，就会自动删除。
     *      1.过期 key 集合，
     *          ①定时策略，定时删除是集中处理：redis 会将每个设置了过期时间的 key 放入到一个独立的字典中，以后会定时遍历这个字典来删除到期的 key
     *          ②惰性策略，惰性删除是零散处理：所谓惰性策略就是在客户端访问这个 key 的时候，redis 对 key 的过期时间进行检查，如果过期了就立即删除。
     *      2.定时扫描策略，Redis 默认会每秒进行十次过期扫描，过期扫描不会遍历过期字典中所有的 key，而是采用了一种简单的贪心策略。
     *        贪心策略：
     *          ①从过期字典中随机 20 个 key；
     *          ②删除这 20 个 key 中已经过期的 key；
     *          ③如果过期的 key 比率超过 1/4，那就重复步骤①
     *          注意：同时，为了保证过期扫描不会出现循环过度，导致线程卡死现象，算法还增加了扫描时间的上限，默认不会超过 25ms。
     *              所以业务开发人员一定要注意过期时间，如果有大批量的 key 过期，要给过期时间设置一个随机范围，而不能全部在同一时间过期。
     *      3.从库的过期策略：
     *          从库不会进行过期扫描，从库对过期的处理是被动的。主库在 key 到期时，会在 AOF 文件里增加一条 del 指令，同步到所有的从库，从库通过执行这条 del 指令来删除过期的 key。
     *          因为指令同步是异步进行的，所以主库过期的 key 的 del 指令没有及时同步到从库的话，会出现主从数据的不一致，主库没有的数据在从库里还存在。
     * 六、Redis 内存淘汰机制:
     *         Redis 数据库可以通过配置文件来配置最大缓存，当写入的数据发现没有足够的内存可用的时候，Redis 会触发内存淘汰机制。
     *         Redis 为了满足多样化场景，提供了八种策略，可以在 redis.config 文件中配置。
     *       （1）volatile-lru：从已设置过期时间的数据集中挑选最近最少使用的数据淘汰
     *       （2）volatile-ttl：从已设置过期时间的数据集中挑选将要过期的数据淘汰
     *       （3）volatile-random：从已设置过期时间的数据集中任意选择数据淘汰
     *       （4）volatile-lfu：从已设置过期时间的数据集中挑选使用频率最低的数据淘汰
     *       （5）allkeys-lru：从所有数据集中挑选最近最少使用的数据淘汰
     *       （6）allkeys-lfu：从所有数据集中挑选使用频率最低的数据淘汰
     *       （7）allkeys-random：从所有数据集中任意选择数据淘汰
     *       （8）noenviction：不回收任何数据，返回一个写操作的错误信息。这也是默认策略
     * 七、Redis 持久化：Redis 的持久化机制有两种，第一种是快照，第二种是AOF日志。
     *      1、快照：快照是一次全量备份，快照是内存数据的二进制序列化形式，在存储上非常紧凑，Redis 使用操作系统的多进程 COW(Copy On Write) 机制来实现快照持久化
     *      2、AOF日志：AOF日志是连续的增量备份，AOF日志记录的是内存数据修改的指令记录文本
     *            注意：AOF 日志在长期的运行过程中会变的无比庞大，数据库重启时需要加载 AOF 日志进行指令重放，这个时间就会无比漫长。所以需要定期进行 AOF 重写，给 AOF 日志进行瘦身。
     *
     */
}
