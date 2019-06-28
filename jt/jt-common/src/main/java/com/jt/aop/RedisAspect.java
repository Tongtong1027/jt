package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component//将对象交给spring容器
@Aspect	  //标识切面
public class RedisAspect {
	
	//注入redis哨兵工具API
	@Autowired(required = false)
	private JedisCluster jedis;
	//private RedisService jedis;
	
	//容器初始化时不需要实例化该对象
	//只有用户使用时才初始化
	//一般工具类中添加该注解
	/*@Autowired(required = false)
	private ShardedJedis jedis;*/
	//private Jedis jedis;
	
	//使用该方法可以直接获取注解对象
	//当拦截了cache_find这个注解时,标识了此注解下方法用的通知是around通知(标识了注解切面生效)
	/**
	 * 1.判断用户的key的类型
	 * @param cache_find
	 * @return
	 */
	@Around("@annotation(cache_find)")//around里面定义了切入点表达式
	//execution(返回值类型 包名.类型.方法名(参数列表))
	//@annotation会扫面当前业务中的全部注解,并满足形参中的类型的注解
	public Object around(ProceedingJoinPoint joinPoint,Cache_Find cache_find) {
		//1.获取key的值
		String key = getKey(joinPoint,cache_find);
		//2.根据key查询缓存
		String result = jedis.get(key);
		Object data = null;
		try {
			//从数据库查数据,存入缓存
			if(StringUtils.isEmpty(result)) {
				//如果结果为null,表示缓存中没有数据
				//查询数据库
				data = joinPoint.proceed();//表示业务方法执行
				//将数据转化为JSON串
				String json = 
						ObjectMapperUtil.toJSON(data);
				//存入redis数据库
				//判断用户是否设定超时时间
				if(cache_find.seconds() == 0) 
					//表示不要超时
					jedis.set(key, json);
				else 
					jedis.setex(key, cache_find.seconds(), json);
				System.out.println("第一次查询数据库!!!");
				return data;
				
			}else {
				Class targetClass = getClass(joinPoint);
				//如果缓存中有数据,则将json串转化为对象返回
				data = ObjectMapperUtil.jSontoObject(result, targetClass);
				System.out.println("AOP查询缓存!!!");
				return data;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	//获取返回值的类型
	private Class getClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = 
				(MethodSignature)joinPoint.getSignature();
		return signature.getReturnType();
	}
	private String getKey(ProceedingJoinPoint joinPoint,Cache_Find cache_find) {
		//1.获取key的类型
		KEY_ENUM key_ENUM = cache_find.keyType();
		//2.判断key类型
		if(key_ENUM.equals(KEY_ENUM.EMPTY)) {
			//表示使用用户自己的key
			return cache_find.key();
		}
		//表示用户的key需要拼接 key+"_"+第一个参数
		String strArgs = String.valueOf(joinPoint.getArgs()[0]);
		String key = cache_find.key()+"_"+strArgs;
		return key;
		
	}
}
