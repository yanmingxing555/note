package com.fomp.note.util.snowflake.hutool;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hutool工具包根据雪花算法生成id
 */
@Slf4j
@Component
public class IdGeneratorSnowflake{
	private long workerId = 0;
	private long datacenterId = 1;
	private Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

	public synchronized long snowflakeId(){
		return snowflake.nextId();
	}

	public synchronized long snowflakeId(long workerId, long datacenterId){
		Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
		return snowflake.nextId();
	}

	public static void main(String[] args){
	    IdGeneratorSnowflake idGenerator = new IdGeneratorSnowflake();
		System.out.println(idGenerator.snowflakeId());
        
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
		for (int i = 1; i <= 20; i++){
			threadPool.submit(() -> {
				System.out.println(idGenerator.snowflakeId());
			});
		}
		threadPool.shutdown();

	}
}
