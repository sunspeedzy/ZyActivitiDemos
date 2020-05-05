package com.imooc.activiti.dbentity;

import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 演示内容：
 * 向运行时流程数据表中插入数据
 * 
 * @author zhangyan
 *
 */
public class DbRuntimeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRuntimeTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql-idhis.cfg.xml");
	
	@Test
	public void testRuntime() {
		LOGGER.info("向ACT_RU_EXECUTION、ACT_RU_TASK、ACT_RU_VARIABLES 插入数据");
		activitiRule.getRepositoryService().createDeployment()
				.name("二次审批流程")
				.addClasspathResource("second_approve.bpmn20.xml")
				.deploy();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance second_approve = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("second_approve", variables);
	}
	
	@Test
	public void testSetOwner() {
		LOGGER.info("向ACT_RU_IDENTITYLINK 插入数据");
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("second_approve").singleResult();
		taskService.setOwner(task.getId(), "user1");
	}
	
	@Test
	public void testMessage() {
		LOGGER.info("向ACT_RU_EVENT_SUBSCR 插入数据，但不会插入 EXECUTION_ID_ 和 PROC_INST_ID_");
		activitiRule.getRepositoryService().createDeployment()
			.addClasspathResource("my-process-message.bpmn20.xml")
			.deploy();

		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("my-process");
	}
	
	@Test
	public void testMessageReceived() {
		LOGGER.info("向ACT_RU_EVENT_SUBSCR 插入数据，会插入 EXECUTION_ID_ 和 PROC_INST_ID_");
		activitiRule.getRepositoryService().createDeployment()
			.addClasspathResource("my-process-message-received.bpmn20.xml")
			.deploy();
		
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("my-process-messagereceived");
	}
	
	@Test
	public void testJob() throws InterruptedException {
		LOGGER.info("向ACT_RU_TIMER_JOB 插入数据");
		activitiRule.getRepositoryService().createDeployment()
			.addClasspathResource("my-process-job.bpmn20.xml")
			.deploy();
		
		Thread.sleep(1000 * 30L);
	}
}
