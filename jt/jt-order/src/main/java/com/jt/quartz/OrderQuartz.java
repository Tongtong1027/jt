package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.pojo.Order;
import com.jt.mapper.OrderMapper;

//准备订单定时
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/**当用户订单提交30分钟后,如果还没有支付.则将状态改为6交易关闭
	 * sql:update tb_order set status =6,updated=#{date}
	 * 	   where status=1 and created< now -30
	 * 现在时间 - 订单创建时间 > 30分钟  则超时
	 * new date - 30 分钟 > 订单创建时间
	 */
	//当程序执行时 执行该方法
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//设定30分钟超时
		Calendar calendar = Calendar.getInstance();//当前时间
		calendar.add(Calendar.MINUTE, -30);
		Date timeOutDate = calendar.getTime();//超时时间
		
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1)
			         .lt("created", timeOutDate);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务执行成功");
	}
}
