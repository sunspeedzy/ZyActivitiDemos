package com.imooc.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试在流程定义文件中使用 Bean
 * 
 * @author zhangyan_g
 *
 */
public class HelloBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);
	
	public void sayHello() {
		LOGGER.info("测试在流程定义文件中使用 Bean sayHello");
	}

}
