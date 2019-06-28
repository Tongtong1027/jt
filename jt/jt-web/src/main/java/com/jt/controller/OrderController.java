package com.jt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

import sl.org.objectweb.asm.tree.TryCatchBlockNode;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(timeout = 3000,check = false)
	private DubboOrderService orderService;
	@Reference(timeout = 3000,check = false)
	private DubboCartService cartService;
	
	/**
	 * 跳转订单确认页面
	 * url:http://www.jt.com/order/create.html
	 * 页面名称:order-cart.jsp
	 * 页面取值:items="${carts}"
	 */
	@RequestMapping("/create")
	public String orderCreate(Model model) {
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cart = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", cart);
		return "order-cart";
	}
	/**
	 * url:http://www.jt.com/order/submit
	 * @param order
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult insertOrder(Order order) {
		try {
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			String orderId = orderService.insertOrder(order);
			if(!StringUtils.isEmpty(orderId)) {
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	/**
	 * 根据订单信息,查询数据
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order",order);
		return "success";
	}
	
}
