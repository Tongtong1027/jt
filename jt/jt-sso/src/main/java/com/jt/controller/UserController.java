package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster JedisCluster;
	
	/**
	 * 业务说明
	 * Ajax校验用户是否存在
	 * http://sso.jt.com/user/check/{param}/{type}
	 * 返回值:SysResult
	 * 由于是跨域请求,所以返回值必须特殊处理callback(json)
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback) {
		JSONPObject object = null;
		
		try {
			boolean flag = userService.checkUser(param,type);
			object = new JSONPObject(callback, SysResult.ok(flag));
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONPObject(callback, SysResult.fail());
		}
		return object;
	}
	@RequestMapping("/register")
	public SysResult saveUser(String userJson) {
		try {
			//将json格式转为对象
			User user = 
			ObjectMapperUtil.jSontoObject(userJson, User.class);
			
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**利用跨域实现用户信息回显
	 * "http://sso.jt.com/user/query/b9ac0e34f3f6258abeaac541b0766e8f*/
	//前端发起ajax请求,验证cookie中的token值
	@RequestMapping("/query/{ticket}")//token值
	public JSONPObject findUserByTicket(@PathVariable String ticket,
			                            String callback) {
		String userJSON = JedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			//回传数据需要经过200判断 SysResult对象
			return new JSONPObject(callback, SysResult.fail());
		}
		return new JSONPObject(callback, SysResult.ok(userJSON));
	}
	
}
