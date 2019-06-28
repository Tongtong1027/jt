package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

public interface ItemService {
	
	EasyUIData findItemByPage(Integer page, Integer rows);

	void saveItem(Item item,ItemDesc itemDesc);

	//void deleteItem(Long[] ids);
	
	void deleteById(Long[] ids);

	void updateStatus(Long[] ids,Integer status);

	ItemDesc findItemDescById(Long itemId);

	void updateItem(Item item, ItemDesc itemDesc);

	Item findItemById(Long id);

}
