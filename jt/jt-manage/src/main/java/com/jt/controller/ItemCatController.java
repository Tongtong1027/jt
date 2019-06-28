package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@RequestMapping("/queryItemName")//实现商品分类信息的查询
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatService.findItemCatNameById(itemCatId);
	}
	//查询全部数据的商品分类信息
	//需要获取任意名称的参数,为指定的参数赋值
	//@RequestParam((value="id")  required = true/false 是否必须传值
	@RequestMapping("/list")
	/*public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue = "0") Long parentId){
//		if(parentId==null)
//		parentId=0L;
		//Long parentId = 0L;
		
		return itemCatService.findItemCatByParentId(parentId);
	}*/
	@Cache_Find(key="ITEM_CAT",keyType=KEY_ENUM.AUTO)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue = "0") Long parentId){
		return itemCatService.findItemCatByParentId(parentId);
	}
}
