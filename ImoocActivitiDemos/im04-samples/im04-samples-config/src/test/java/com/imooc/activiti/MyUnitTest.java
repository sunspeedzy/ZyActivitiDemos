package com.imooc.activiti;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * @author zhangyan_g
 *
 */
public class MyUnitTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process.bpmn20.xml"})
	public void test() {
		// 开启MDC
		LogMDC.setMDCEnabled(true);
		
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);

		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		assertEquals("Activiti is awesome!", task.getName());
		
		activitiRule.getTaskService().complete(task.getId());
	}
	
}
