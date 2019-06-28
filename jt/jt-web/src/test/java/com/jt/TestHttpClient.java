package com.jt;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	@Autowired
	private CloseableHttpClient client;
	/**
	 * 测试HttpClient
	 * 1.实例化httpClient对象
	 * 2.定义http请求路径url/uri
	 * 3.定义请求方式get/post
	 * 4.利用API发起http请求
	 * 5.获取返回值以后，判断状态的信息 200
	 * 6.获取响应的数据
	 */
	@Test
	public void testGet() throws Exception{
		
		/*CloseableHttpClient client = 
				HttpClients.createDefault();*/
		String url = "https://www.baidu.com";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = 
				client.execute(httpGet);
		if(response.getStatusLine()
				   .getStatusCode() == 200) {
			System.out.println("恭喜你请求成功!!!");
			HttpEntity httpEntity= response.getEntity();
			String result = EntityUtils.toString(httpEntity);
			System.out.println(result);
		}else {
			throw new RuntimeException();
		}
		
	}
	@Autowired
	private HttpClientService httpClientService;
	@Test
	public void testUtil() {
		String url = "http://www.baidu.com";
		String result = 
				httpClientService.doGet(url);
		System.out.println(result);
	}
}
