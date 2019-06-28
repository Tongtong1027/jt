package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;

//表示redis配置类,交给spring管理


@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String redisNodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		//1.按照，号拆分     ：号拆分  获取IP和端口
		String[] strNode = redisNodes.split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		for (String nodeArgs : strNode) {
			String host = nodeArgs.split(":")[0];
			int port = Integer.parseInt(nodeArgs.split(":")[1]);
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
	}
	
	
	
	
	
	
	
	
	
	
	/*@Value("${redis.sentinels}")
	private String jedisSentinelNodes;
	@Value("${redis.sentinel.masterName}")
	private String masterName;
	@Bean
	public JedisSentinelPool  jedisSentinelPool(){
		Set<String> sentinels = new HashSet<>();
		sentinels.add(jedisSentinelNodes);
		return new JedisSentinelPool(masterName, sentinels);
	}*/
}
