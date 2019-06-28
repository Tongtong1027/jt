package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {
	@Test
	public void test01() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.136.130",7000));
		nodes.add(new HostAndPort("192.168.136.130",7001));
		nodes.add(new HostAndPort("192.168.136.130",7002));
		nodes.add(new HostAndPort("192.168.136.130",7003));
		nodes.add(new HostAndPort("192.168.136.130",7004));
		nodes.add(new HostAndPort("192.168.136.130",7005));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("tongtong", "集群搭建完成");
		System.out.println("获取集群数据："+cluster.get("tongtong"));
		
	}
}
