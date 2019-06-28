package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private JedisCluster jedisCluster;
	/*@Autowired
	private UserService userService;*/
	//导入dubbo的用户接口
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;
	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName) {
		return moduleName;
	}
	/**
	 * 前台数据：{password:_password,username:_username,phone:_phone}
	 * password:_password，username:_username，phone:_phone
	 * K,V结构。
	 * SpringMvc中通过request.getParameters()获取里面的参数
	 * 再通过set方法进行赋值，(User必须有set方法)
	 * 
	 */
	@RequestMapping("/doRegister")//点击注册事件
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**
	 * 实现用户登录
	 * 利用Response对象将Cookie数据写入客户端
	 * cookie生命周期
	 * cookie.setMaxAge(0);立即删除
	 * cookie.setMaxAge(值>0);能够存活多久 单位/秒
	 * cookie.setMaxAge(-1) 当会话结束后删除
	 * 
	 * cookie.setPath("/abc");cookie的权限
	 * 
	 * 例子:
	 * 页面位置:
	 * 		www.jd.com/abc/a.html
	 * 		www.jd.com/b.html
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")//点击登录事件
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		try {
			//调用sso系统获取秘钥
			String token = userService.findUserByUP(user);
			//判断数据是不为null时,将数据保存到cookie中
			
			if(!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setMaxAge(7*24*3600);//生命周期
				cookie.setDomain("jt.com");//要求所有的xxxx.jt.com(实现数据的共享)
				cookie.setPath("/");
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	/**实现用户登出操作
	 * 1.删除redis中的用户信息 request对象~~cookie中~~JT_TICKET
	 * 2.*/
	@RequestMapping("/logout")//点击退出事件
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if(cookies.length !=0) {
			String token = null;
			for(Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
			//判断token数据是否有值，删除redis/删除cookie
			if(!StringUtils.isEmpty(token)) {
				jedisCluster.del(token);
				Cookie cookie = new Cookie("JT_TICKET","");
				cookie.setMaxAge(0);//立即删除
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				response.addCookie(cookie);
			}
		}
		//当用户登出时，页面重定向到系统首页
		return "redirect:/";
	}
	
}
