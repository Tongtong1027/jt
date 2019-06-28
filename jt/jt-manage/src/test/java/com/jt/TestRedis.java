package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

import redis.clients.jedis.Jedis;

public class TestRedis {

	//1.String类型操作方式 按配置文件3处  防火墙
	//IP: 端口号:
	@Test
	public void testString() {
		Jedis jedis = 
				new Jedis("192.168.136.130",6379);
		jedis.set("an","tong");
		jedis.set("chen", "baba");
		//jedis.mset("aaa","aaa","bbb","bbb");
		jedis.expire("aaa", 100);
		System.out.println(jedis.get("bbb"));
		System.out.println(jedis.strlen("an"));
		System.out.println(jedis.exists("an"));
	}
	//2.设定数据超时的方法 2种
	//分布式锁
	@Test
	public void testTimeOut() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.136.130",6379);
		jedis.setex("aa", 2, "aa");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		//当key不存在时操作正常,当key存在时,则操作失败
		Long result = jedis.setnx("aa", "bb");
		System.out.println("获取输出数据:"+result+jedis.get("aa"));

	}
	//1.实现对象转化JSON
	@Test
	public void objectToJson() throws IOException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L).setItemDesc("测试方法").setCreated(new Date());
		//item.setId(100L).setTitle("测试数据!!!");
		ObjectMapper mapper = new ObjectMapper();
		//转化JSON时必须get/set方法
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		//将json串转化为对象
		ItemDesc desc = mapper.readValue(json, ItemDesc.class);
		System.out.println("测试对象:"+desc);
	}
	//实现List集合与JSON转化
	@Test
	public void testListTOJSON() throws IOException {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(1001L).setItemDesc("测试方法1");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(1002L).setItemDesc("测试方法2");
		List<ItemDesc> itemDescList = new ArrayList<>();

		itemDescList.add(itemDesc1);
		itemDescList.add(itemDesc2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(itemDescList);

		//将数据保存到redis中
		Jedis jedis = new Jedis("192.168.136.130",6379);
		jedis.set("itemDescList", json);

		//从redis中获取数据
		String result = jedis.get("itemDescList");
		System.out.println(result);
		System.out.println("----------------------");
		List<ItemDesc> descList = mapper.readValue(result, itemDescList.getClass());
		System.out.println(descList);

	}
	/**3.利用Redis保存业务数据  数据库
	 * 数据库数据:对象 Object
	 * String类型要求只能存储字符串类型
	 * item ~~JSON~~字符串*/
	@Test
	public void testSetObject() {
		Item item = new Item();
		item.setId(100L).setTitle("测试数据!!!");
		Jedis jedis = new Jedis("192.168.136.130",6379);
	}
	
	//研究转化过程
	class User{
		private Integer id;
		private String name;
		private Integer age;
		private String sex;
		public Integer getId11() {
			return id;
		}
		public void setId11(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		
		}
	/**
	 * 1.首先获取对象的getXXXX方法.
	 * 2.将get去掉,之后首字母小写获取属性的名称
	 * 3.之后将属性名称 : 属性的值进行拼接.
	 * 4.形成json串(字符串)
	 * @throws JsonProcessingException
	 */
	//User转json串
	@Test
	public void userToJSON() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId11(1000);
		user.setAge(20);
		user.setName("童童");
		user.setSex("女");
		String userJson = mapper.writeValueAsString(user);
		System.out.println(userJson);
	}
	/**
	 * 1.获取userJSON串
	 * 2.通过json串获取json中key
	 * 2.根据class类型的反射机制实例化对象
	 * 3.根据key调用setKey方法为对象赋值.
	 * 4.最终生成对象.
	 * 5.可以利用
	 * 	 @JsonIgnoreProperties(ignoreUnknown = true)
	 * 	 注解忽略未知属性	
	 * 
	 * @throws IOException
	 */
	@Test
	public void jsonToUser() throws IOException {
		//该方法准备json串
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId11(1000);
		user.setName("json测试");
		user.setAge(18);
		user.setSex("男");
		String json = 
				mapper.writeValueAsString(user);
		//以下方法实现了数据的转化
		User user2 = mapper.readValue(json, User.class);
		System.out.println(user2);
	}

}




