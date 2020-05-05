package com.imooc.activiti.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示内容：
 * 1. 使用RepositoryService API部署流程定义文件时，如何引起资源数据的插入
 * 2. 使用原生的最底层的 ByteArrayEntity对应的Manager去写入数据库
 * 
 * @author zhangyan
 *
 */
public class DbGeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbGeTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
	
	@Test
	public void testByteArray() {
		LOGGER.info("使用RepositoryService API部署流程定义文件时，引起资源数据的插入");
		activitiRule.getRepositoryService().createDeployment().name("测试部署")
				.addClasspathResource("my-process.bpmn20.xml").deploy();
	}
	
	@Test
	public void testByteArrayInsert() {
		LOGGER.info("使用原生的最底层的 ByteArrayEntity对应的Manager去写入数据库");
		ManagementService managementService = activitiRule.getManagementService();
		Object o = managementService.executeCommand(new Command<Object>() {

			@Override
			public Object execute(CommandContext commandContext) {
				ByteArrayEntity entity = new ByteArrayEntityImpl();
				entity.setName("test");
				entity.setBytes("test message".getBytes());
				// 获取 ByteArrayEntityManager实例插入ByteArrayEntity数据
				commandContext.getByteArrayEntityManager().insert(entity);;

				return null;
			}
			
		});
	}
}
