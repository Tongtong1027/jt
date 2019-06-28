package com.jt.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 如何标识配置类
 * 需要配置bean注解  
 * @author Administrator
 *
 */
/*@Configuration
@PropertySource("classpath:/properties/redisnodes.properties")
public class RedisShardsConfig {
	@Value("${redis.nodes}")
	private String nodes;//ip:端口号,ip:端口号...
	
	@Bean
	public ShardedJedis getShards() {
		List<JedisShardInfo> shards = 
				new ArrayList<JedisShardInfo>();
		//将nodes中的数据进行分组 
		String[] node = nodes.split(",");//{"IP:端口","IP:端口"}
		for (String nodeArgs : node) {
			String[] args = nodeArgs.split(":");////{IP,端口}
			String nodeIP = args[0];
			int nodePort = Integer.parseInt(args[1]);
			JedisShardInfo jedisShardInfo =
			new JedisShardInfo(nodeIP,nodePort);
			shards.add(jedisShardInfo);
		}
		return new ShardedJedis(shards);
	}
}*/
