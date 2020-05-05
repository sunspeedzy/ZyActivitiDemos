package com.imooc.activiti.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * 测试执行自定义SQL
 * 
 * @author zhangyan
 *
 */
public interface MyCustomMapper {
	@Select("SELECT * FROM ACT_RU_TASK")
	public List<Map<String, Object>> findAll();
}
