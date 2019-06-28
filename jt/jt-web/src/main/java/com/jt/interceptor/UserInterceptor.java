package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
@Component  //将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor {
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4版本中要求必须重写3个方法,不管是否有效，是否需要
	 * 在spring5版本中在接口中添加default属性,则省略不写
	 */
	
	/**
	 * 返回值结果:
	 * true:拦截放行
	 * false:请求拦截 重定向到登录页面
	 * 
	 * 业务逻辑:
	 * 1.获取cookie数据
	 * 2.从Cookie中获取用户token(TICKET)
	 * 3.判断redis缓存服务器是否有数据
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		//1.获取Cookie信息
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//2.判断token是否有效
		if(!StringUtils.isEmpty(token)) {
			//4.判断redis中是否有数据
			String userJSON = jedisCluster.get(token);
			
			if(!StringUtils.isEmpty(userJSON)) {
				//将userJSON转化为user对象
				User user = ObjectMapperUtil.jSontoObject(userJSON, User.class);
				//将对象加到request域中
			
				UserThreadLocal.set(user);
				//防止系统意外，key值不能为USER
				//redis中有用户数据，拦截放行
				return true;
			}
		}
		//3.重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		return false;//表示拦截
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
