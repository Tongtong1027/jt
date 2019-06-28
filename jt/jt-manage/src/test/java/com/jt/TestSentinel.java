package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	//测试哨兵get/set操作
	@Test
	public void test01() {
		//masterName 代表主机的变量名称
		//sentines Set<String> IP:端口
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.136.130:26379");//添加哨兵
		JedisSentinelPool sentinelPool = 
				new JedisSentinelPool("mymaster",sentinels);
		//配置主节点
		Jedis jedis = sentinelPool.getResource();
		jedis.set("cc", "端午节过后没假了");
		System.out.println(jedis.get("cc"));
		jedis.close();//关闭连接
	}
}
