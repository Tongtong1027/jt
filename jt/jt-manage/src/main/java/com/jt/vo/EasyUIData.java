package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**该类封装了json格式的串*/
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIData {
	private Integer total;//记录总数
	private List<Item> rows;//展现数据的集合
	
}
