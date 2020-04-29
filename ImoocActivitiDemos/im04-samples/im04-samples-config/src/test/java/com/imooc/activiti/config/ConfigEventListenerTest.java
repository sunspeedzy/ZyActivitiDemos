package com.imooc.activiti.config;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imooc.activiti.event.CustomEventListenerAddedInCode;


/**
 * EventLog 测试
 * 
 * @author zhangyan_g
 *
 */
public class ConfigEventListenerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigEventListenerTest.class);

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_eventListener.cfg.xml");

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process.bpmn20.xml"})
	public void test() {
		ProcessInstance processInstance = activitiRule
				.getRuntimeService().startProcessInstanceByKey("my-process");
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		activitiRule.getTaskService().complete(task.getId());
		
		// 在代码中添加一个自定义监听器
		activitiRule.getRuntimeService().addEventListener(
				new CustomEventListenerAddedInCode(), ActivitiEventType.CUSTOM);
		// 发出自定义事件
		activitiRule.getRuntimeService().dispatchEvent(
				new ActivitiEventImpl(ActivitiEventType.CUSTOM));
		
	}
}
