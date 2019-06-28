package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.jt.pojo.User;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
@Service
public class UserServiceImpl implements UserService{
	/*@Autowired
	private HttpClientService httpClientService;
	@Override
	public void saveUser(User user) {
		String url = "http://sso.jt.com/user/register";
		//密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		Map<String,String> params = new HashMap<>();
		String userJSON = ObjectMapperUtil.toJSON(user);//json格式
		params.put("userJson", userJSON);
		//传到sso后台
		String result = httpClientService.doPost(url, params);
		//result是json格式的串
		//接收sso后台传回的结果并转成对象，封装到SysResult对象
		SysResult sysResult = 
		ObjectMapperUtil.jSontoObject(result, SysResult.class);
		//如果发生错误，手动抛出异常
		if(sysResult.getStatus() == 201) {
			throw new RuntimeException();
		}
	}
*/
}
