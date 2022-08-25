package com.fomp.note.redis.jedispool;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * redis部署方式分为单节点与集群部署，
 * JedisPool连一台Redis，
 * ShardedJedisPool连Redis集群，通过一致性哈希算法决定把数据存到哪台上，算是一种客户端负载均衡，Redis 3.0之后支持服务端负载均衡
 */
public class RedisAppPool {
	/** 
	* 非切片客户端链接
	*/
	private Jedis jedis;
	/**
	* 非切片链接池
	*/
	private JedisPool jedisPool;
	/**
	* 切片客户端链接
	*/
	private ShardedJedis shardedJedis;
	/**
	* 切片链接池
	*/
	private ShardedJedisPool shardedJedisPool;
	
	private String ip = "10.10.10.157";
	/**
	* 构造函数
	*/
	public RedisAppPool() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}
 
	private void initialPool() {
		//池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);// 最大连接数
		config.setMaxIdle(5);// 最大空闲连接数
		config.setMaxWaitMillis(1000L);//获取连接时的最大等待毫秒数
		config.setTestOnBorrow(false);//向连接池借用连接时是否做连接有效性检测（ping），无效连接会被移除，每次借用多执行一次 ping 命令，默认值为 false。
		config.setTestOnReturn(false);//向连接池归还连接时是否做连接有效性检测（ping），无效连接会被移除，每次归还多执行一次 ping 命令，默认值为 false。
		jedisPool = new JedisPool(config, ip, 6379,60000,"123456" );

	}
	
	/**
	* 初始化切片池
	*/
	private void initialShardedPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);// 最大连接数
		config.setMaxIdle(5);// 最大空闲连接数
		config.setMaxWaitMillis(1000L);//获取连接时的最大等待毫秒数
		config.setTestOnBorrow(false);//向连接池借用连接时是否做连接有效性检测（ping），无效连接会被移除，每次借用多执行一次 ping 命令，默认值为 false。
		config.setTestOnReturn(false);//向连接池归还连接时是否做连接有效性检测（ping），无效连接会被移除，每次归还多执行一次 ping 命令，默认值为 false。
		//slave 连接, 这里实现了集群的功能，配置多个redis服务实现请求的分配进行负载均衡
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo shardInfo = new JedisShardInfo("10.10.10.157", 6379,"master");
		//shardInfo.setPassword("123456");
		shards.add(shardInfo);
		// 构造池
		shardedJedisPool = new ShardedJedisPool(config, shards);		
	}
 
	public  void redisTest(){
		// key检测
		testKey();
		// string检测
		testString();
		// list检测
	    testList();
		// set检测
		testSet();
		// sortedSet检测
		testSortedSet();
		// hash检测
		testHash();
		//使用Jedis连接池之后，在每次用完连接对象后一定要记得把连接归还给连接池。
		//Jedis对close方法进行了改造，如果是连接池中的连接对象，调用Close方法将会是把连接对象返回到对象池，若不是则关闭连接。
		jedis.close();
		shardedJedis.close();
	}
 
	private void testKey() {
		System.out.println("=============key==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		System.out.println(jedis.echo("foo"));
		// 判断key否存在
		System.out.println(shardedJedis.exists("foo"));
		shardedJedis.set("key", "values");
		System.out.println(shardedJedis.exists("key"));
	}
 
	private void testString() {
		System.out.println("=============String==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		// 存储数据
		shardedJedis.set("foo", "bar");
		System.out.println(shardedJedis.get("foo"));
		// 若key不存在，则存储
		shardedJedis.setnx("foo", "foo not exits");
		System.out.println(shardedJedis.get("foo"));
		// 覆盖数据
		shardedJedis.set("foo", "foo update");
		System.out.println(shardedJedis.get("foo"));
		// 追加数据
		shardedJedis.append("foo", " hello, world");
		System.out.println(shardedJedis.get("foo"));
		// 设置key的有效期，并存储数据
		shardedJedis.setex("foo", 2, "foo not exits");
		System.out.println(shardedJedis.get("foo"));
		try {
		Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		System.out.println(shardedJedis.get("foo"));
		// 获取并更改数据
		shardedJedis.set("foo", "foo update");
		System.out.println(shardedJedis.getSet("foo", "foo modify"));
		// 截取value的值
		System.out.println(shardedJedis.getrange("foo", 1, 3));
		System.out.println(jedis.mset("mset1", "mvalue1", "mset2", "mvalue2",
		"mset3", "mvalue3", "mset4", "mvalue4"));
		System.out.println(jedis.mget("mset1", "mset2", "mset3", "mset4"));
		System.out.println(jedis.del(new String[] { "foo", "foo1", "foo3" }));		
	}
 
	private void testList() {
		System.out.println("=============list==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		// 添加数据
		shardedJedis.lpush("lists", "vector");
		shardedJedis.lpush("lists", "ArrayList");
		shardedJedis.lpush("lists", "LinkedList");
		// 数组长度
		System.out.println(shardedJedis.llen("lists"));
		// 排序
	   // System.out.println(shardedJedis.sort("lists"));
		// 字串
		System.out.println(shardedJedis.lrange("lists", 0, 3));
		// 修改列表中单个值
		shardedJedis.lset("lists", 0, "hello list!");
		// 获取列表指定下标的值
		System.out.println(shardedJedis.lindex("lists", 1));
		// 删除列表指定下标的值
		System.out.println(shardedJedis.lrem("lists", 1, "vector"));
		// 删除区间以外的数据
		System.out.println(shardedJedis.ltrim("lists", 0, 1));
		// 列表出栈
		System.out.println(shardedJedis.lpop("lists"));
		// 整个列表值
		System.out.println(shardedJedis.lrange("lists", 0, -1));		
	}
 
	private void testSet() {
		System.out.println("=============set==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		// 添加数据
		shardedJedis.sadd("sets", "HashSet");
		shardedJedis.sadd("sets", "SortedSet");
		shardedJedis.sadd("sets", "TreeSet");
		// 判断value是否在列表中
		System.out.println(shardedJedis.sismember("sets", "TreeSet"));
 
		// 整个列表值
		System.out.println(shardedJedis.smembers("sets"));
		// 删除指定元素
		System.out.println(shardedJedis.srem("sets", "SortedSet"));
		// 出栈
		System.out.println(shardedJedis.spop("sets"));
		System.out.println(shardedJedis.smembers("sets"));
		//
		shardedJedis.sadd("sets1", "HashSet1");
		shardedJedis.sadd("sets1", "SortedSet1");
		shardedJedis.sadd("sets1", "TreeSet");
		shardedJedis.sadd("sets2", "HashSet2");
		shardedJedis.sadd("sets2", "SortedSet1");
		shardedJedis.sadd("sets2", "TreeSet1");
		// 交集
		System.out.println(jedis.sinter("sets1", "sets2"));
		// 并集
		System.out.println(jedis.sunion("sets1", "sets2"));
		// 差集
		System.out.println(jedis.sdiff("sets1", "sets2"));		
	}
 
	private void testSortedSet() {
		System.out.println("=============zset==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		// 添加数据
		shardedJedis.zadd("zset", 10.1, "hello");
		shardedJedis.zadd("zset", 10.0, ":");
		shardedJedis.zadd("zset", 9.0, "zset");
		shardedJedis.zadd("zset", 11.0, "zset!");
		// 元素个数
		System.out.println(shardedJedis.zcard("zset"));
		// 元素下标
		System.out.println(shardedJedis.zscore("zset", "zset"));
		// 集合子集
		System.out.println(shardedJedis.zrange("zset", 0, -1));
		// 删除元素
		System.out.println(shardedJedis.zrem("zset", "zset!"));
		System.out.println(shardedJedis.zcount("zset", 9.5, 10.5));
		// 整个集合值
		System.out.println(shardedJedis.zrange("zset", 0, -1));		
	}
 
	private void testHash() {
		System.out.println("=============hash==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		// 添加数据
		shardedJedis.hset("hashs", "entryKey", "entryValue");
		shardedJedis.hset("hashs", "entryKey1", "entryValue1");
		shardedJedis.hset("hashs", "entryKey2", "entryValue2");
		// 判断某个值是否存在
		System.out.println(shardedJedis.hexists("hashs", "entryKey"));
		// 获取指定的值
		System.out.println(shardedJedis.hget("hashs", "entryKey"));
		// 批量获取指定的值
		System.out
		.println(shardedJedis.hmget("hashs", "entryKey", "entryKey1"));
		// 删除指定的值
		System.out.println(shardedJedis.hdel("hashs", "entryKey"));
		// 为key中的域 field 的值加上增量 increment
		System.out.println(shardedJedis.hincrBy("hashs", "entryKey", 123l));
		// 获取所有的keys
		System.out.println(shardedJedis.hkeys("hashs"));
		// 获取所有的values
		System.out.println(shardedJedis.hvals("hashs"));		
	}
}