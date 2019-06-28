package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

//因为需要跳转页面,所以不能使用restController
//如果后期返回值是json串,则在方法上添加@ResponseBody
@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout = 3000,check = false)
	private DubboCartService cartService;
	/**
	 * 1.实现购物车商品列表信息展现
	 * 2.页面取值：${cartList}
	 * @return
	 */
	@RequestMapping("/show")
	public String findCartList(Model model,HttpServletRequest request) {
		User user = UserThreadLocal.get();
		Long userId = user.getId();
		UserThreadLocal.remove();
		List<Cart> cartList =
				cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";//返回页面的逻辑名称
	}
	/**
	 * 实现购物车数量的修改
	 * url地址：http://www.jt.com/cart/update/num/562379/8
	 * 规定：如果url参数中使用restFul风格获取数据时
	 * 接收参数是对象并且属性匹配,则可以使用对象接收
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart,HttpServletRequest request) {
		try {
			User user = UserThreadLocal.get();
			Long userId = user.getId();
			UserThreadLocal.remove();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();		
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**
	 * 实现购物车信息的删除
	 * url: http://www.jt.com/cart/delete/562379.html
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart,HttpServletRequest request) {
		User user = UserThreadLocal.get();
		Long userId = user.getId();
		UserThreadLocal.remove();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到购物车列表页面
		return "redirect:/cart/show.html";
				
	}
	/**
	 * 新增购物车
	 * 页面表单提交  发起POST请求
	 * 携带购物车参数
	 */
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart,HttpServletRequest request) {
		User user = UserThreadLocal.get();
		Long userId = user.getId();
		UserThreadLocal.remove();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//新增数据后,展现购物车信息
		return "redirect:/cart/show.html";
	}
	
}
