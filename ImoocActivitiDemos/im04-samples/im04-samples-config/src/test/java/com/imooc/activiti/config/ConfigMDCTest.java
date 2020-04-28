package com.imooc.activiti.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDC 测试
 * 
 * @author zhangyan_g
 *
 */
public class ConfigMDCTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_mdc.cfg.xml");

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process_mdcerror.bpmn"})
	public void test() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);

		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		assertEquals("Activiti is awesome!", task.getName());
		
		activitiRule.getTaskService().complete(task.getId());
	}
}
