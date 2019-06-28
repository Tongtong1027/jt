package com.jt;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis2 {
	//编辑List集合
	@Test
	public void list() {
		Jedis jedis = new Jedis("192.168.136.130",6379);
		jedis.lpush("list", "1","2","3","4","5");
		System.out.println(jedis.rpop("list"));
	}
	//事务控制
	@Test
	public void testTx() {
		Jedis jedis = new Jedis("192.168.136.130",6379);
		Transaction transaction = jedis.multi(); //开启事务
		try {
			transaction.set("ww", "ww");
			transaction.set("dd", null);
			transaction.exec();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
	}
}
