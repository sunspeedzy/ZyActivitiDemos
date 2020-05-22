package com.imooc.activiti.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map; 
import com.google.common.collect.Maps;

/**
 * 演示内容：
 * 1. 
 * 2. 
 * 
 * @author zhangyan
 *
 */
public class DbHistoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbHistoryTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql-his.cfg.xml");
	
	@Test
	public void testHistory() {
		LOGGER.info("向表中插入数据");
		activitiRule.getRepositoryService()
				.createDeployment().name("测试部署")
				.addClasspathResource("my-process.bpmn20.xml").deploy();
		// 启动流程，并传入参数
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key0", "value0");
		variables.put("key1", "value1");
		variables.put("key2", "value2");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("my-process", variables);
		// 在UserTask执行前，修改变量 key1
		runtimeService.setVariable(processInstance.getId(), "key1", "value1_1");
		// 为 UserTask添加Owner、Attachment、Comment
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setOwner(task.getId(), "user1");
		// 在task中调用setOwner方法添加Owner不能在 act_hi_identitylink 表中体现
//		task.setOwner("user1");
		taskService.createAttachment("url", 
				task.getId(), processInstance.getId(),
				"name", "desc", "/url/test.png");
		taskService.addComment(task.getId(),
				processInstance.getId(), "record note1");
		taskService.addComment(task.getId(),
				processInstance.getId(), "record note2");
		// 通过FormService提交表单，数据会存储在 Detail历史表中，
		// 而使用TaskService提交则不会把数据存储在Detail历史表中
		Map<String, Object> properties = Maps.newHashMap();
		properties.put("key2", "value2_1");
		properties.put("key3", "value3_1");
		activitiRule.getTaskService().complete(task.getId(),
				properties);
	}
	
}
