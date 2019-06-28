package com.jt.util;

import com.jt.pojo.User;

//ThreadLocal是线程安全
public class UserThreadLocal {
	/**
	 * 如何存取多个数据?? Map集合
	 * ThreadLocal<Map<k,v>>
	 */
	private static ThreadLocal<User> thread = 
			new ThreadLocal<>();
	//新增方法
	public static void set(User user) {
		thread.set(user);
	}
	//获取数据
	public static User get() {
		return thread.get();
	}
	
	//使用threadLocal切记关闭,防止内存泄漏
	public static void remove() {
		thread.remove();
	}
}
