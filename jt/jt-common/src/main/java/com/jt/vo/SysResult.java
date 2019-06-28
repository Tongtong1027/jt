package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**此类表示系统的返回值对象*/
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	
	private Integer status;//200表示成功 201表示失败
	private String msg;	//后台返回值数据提示
	private Object data;//后台返回任意数据
	
	public static SysResult ok() {
		return new SysResult(200,null,null);
	}
	public static SysResult ok(Object data) {
		return new SysResult(200,null,data);
	}
	public static SysResult ok(String msg,Object data) {
		return new SysResult(200,msg,data);
	}
	/*假如用户传递的是String类型的data数据，系统会自动调用此方法。
	public static SysResult ok(String msg) {
		return new SysResult(200,msg,null);
	}*/
	
	public static SysResult fail() {
		return new SysResult(201,null,null);
	}
	public static SysResult fail(Object data) {
		return new SysResult(201,null,data);
	}
	public static SysResult fail(String msg,Object data) {
		return new SysResult(201,msg,data);
	}
	
}	
