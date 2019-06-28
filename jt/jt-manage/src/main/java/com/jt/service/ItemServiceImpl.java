package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		//1.获取总记录数
		Integer total = itemMapper.selectCount(null);

		/**
		 * 2.分页之后回传数据
		 * sql: select * from tb_item limit 起始位置,查询行数
		 * 第1页:  20
		 * 	select * from tb_item limit 0,20
		 * 第2页:  
		 * 	select * from tb_item limit 20,20
		 * 第3页:
		 *  select * from tb_item limit 40,20
		 * 第N页:
		 * 	 select * from tb_item 
		 * 			limit (page-1)rows,rows
		 */
		//计算起始位置
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		
		return new EasyUIData(total,itemList);
	}
	@Transactional//添加事务的控制
	@Override
	//name=itemDesc  value=<div>页面信息</div>
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		//mybatis中如果设定主键自增,则新增时Id会自动回传
		//sql:insert into xxx values(xxx)
		//select LAST_INSERT_ID();之后将数据交给对象
		itemMapper.insert(item);
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}
	/**
	 * rollbackFor = "包名.类名.名称.class"
	 * rollbackFor = "异常类型"	遇到异常回滚
	 * norollbackFor = "异常类型" 遇到异常不回滚
	 * 
	 * readOnly = true 不允许修改数据库
	 * propagation 事务传播属性
	 * 				默认值REQUIRED 必须添加事务,事务合并
	 * 				REQUIRED_NEW必须新建一个
	 * itemSave(); 一个事务控制			
	 * itemUpdate();一个事务控制
	 * 				
	 * 				SUPPORTS 表示事务支持的
	 * 
	 * itemSave(); 一个事务控制		 查询之前有事务添加一个事务
	 * findItem();	不要事务
	 * 
	 * Spring中默认的事务控制策略
	 * 	1.检查异常/编译异常   不负责事务控制   解决方法：throw new RuntimeException(e)
	 * 	2.运行时异常/error 回滚事务
	 *  */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		//同时修改2张表
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
		
	}
	
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}
	
	@Override
	public void deleteById(Long[] ids) {
		//1.手动删除
		//itemMapper.deleteItem(ids);
		//2.自动删除
		List<Long> itemList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(itemList);
		itemDescMapper.deleteBatchIds(itemList);
	}
	/*
	 * sql: update tb_item 
	 * 		set status=#{status},
	 * 		updated = #{updated} 
	 * 		where id in (100,200,300....)
	 * */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		List<Long> longIds = Arrays.asList(ids);
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("id",longIds);
		itemMapper.update(item, updateWrapper);
	}
	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}
	
	
	
	
	
	
	
}
