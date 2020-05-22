package com.imooc.activiti.config;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Activiti与Spring Framework集成 测试
 * 
 * @author zhangyan_g
 *
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ConfigSpringTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigSpringTest.class);

	// 通过Spring的方式获取 ActivitiRule，activiti-context.xml中配置，再自动注入进来
	@Rule
	@Autowired
	public ActivitiRule activitiRule;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private TaskService taskService;
	
	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process_spring.bpmn"})
	public void test() throws InterruptedException {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		Task task = taskService.createTaskQuery().singleResult();
		taskService.complete(task.getId());
	}
}
