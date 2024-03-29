package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

//编辑工具类实现对象与json转化
public class ObjectMapperUtil {
	//对象转化为json
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static String toJSON(Object target) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查异常转化为运行时异常
			throw new RuntimeException();
		}
		return json;
	}
	//json转化为对象
	public static <T> T jSontoObject(String json,
								Class<T> targetClass){
		T target = null;
		try {
			target = 
			MAPPER.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查异常转化为运行时异常
			throw new RuntimeException();
		}
		return target;
	}
}
