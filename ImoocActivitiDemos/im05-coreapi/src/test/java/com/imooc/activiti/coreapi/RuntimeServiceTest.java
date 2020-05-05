package com.imooc.activiti.coreapi;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;


/**
 * 测试 RepositoryService
 * 
 * @author zhangyan_g
 *
 */
public class RuntimeServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testStartProcessByKey() {
		LOGGER.info("测试流程运行控制服务，根据流程Key启动流程实例");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		LOGGER.info("processInstance = {}", processInstance);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testStartProcessById() {
		LOGGER.info("测试流程运行控制服务，根据流程定义ID启动流程实例");
		ProcessDefinition processDefinition = activitiRule.getRepositoryService()
				.createProcessDefinitionQuery().singleResult();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(), variables);
		LOGGER.info("processInstance = {}", processInstance);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testProcessInstanceBuilder() {
		LOGGER.info("测试流程运行控制服务，根据 BusinessKey和变量 启动流程实例");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstanceBuilder processInstanceBuilder =
				runtimeService.createProcessInstanceBuilder();
		ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
				.processDefinitionKey("my-process").variables(variables).start();
		
		LOGGER.info("processInstance = {}", processInstance);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testVariables() {
		LOGGER.info("测试流程运行控制服务，获取和修改流程变量");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		variables.put("key2", "value2");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		LOGGER.info("processInstance = {}", processInstance);
		
		Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
		LOGGER.info("variables1 = {}", variables1);

		runtimeService.setVariable(processInstance.getId(), "key3", "value3");
		runtimeService.setVariable(processInstance.getId(), "key2", "value2_1");
		
		Map<String, Object> variables2 = runtimeService.getVariables(processInstance.getId());
		LOGGER.info("variables2 = {}", variables2);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testProcessInstanceQuery() {
		LOGGER.info("测试流程运行控制服务，如何查询流程实例");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		LOGGER.info("processInstance = {}", processInstance);
		
		ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		LOGGER.info("processInstance1 = {}", processInstance1);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testProcessExecutorQuery() {
		LOGGER.info("测试流程运行控制服务，如何查询流程实例的执行实例");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		LOGGER.info("processInstance = {}", processInstance);
		
		List<Execution> executionList = runtimeService.createExecutionQuery().listPage(0, 100);
		for (Execution execution : executionList) {
			LOGGER.info("execution = {}", execution);
		}
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-trigger.bpmn20.xml"})
	public void testTrigger() {
		LOGGER.info("测试触发流程中的ReceiveTask");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key1", "value1");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		LOGGER.info("processInstance = {}", processInstance);
		
		Execution execution = runtimeService.createExecutionQuery()
				.activityId("someTask")
				.singleResult();
		LOGGER.info("execution = {}", execution);
		
		runtimeService.trigger(execution.getId());
		// 在触发了ReceiveTask后，流程实例执行完毕，在重新查询执行实例时返回为null
		execution = runtimeService.createExecutionQuery()
				.activityId("someTask")
				.singleResult();
		LOGGER.info("execution = {}", execution);
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
	public void testSignalEventReceived() {
		LOGGER.info("测试触发流程中的信号捕获事件 signalCatchEvent");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
//		Map<String, Object> variables = Maps.newHashMap();
//		variables.put("key1", "value1");
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		LOGGER.info("processInstance = {}", processInstance);
		
		Execution execution = runtimeService.createExecutionQuery()
				.signalEventSubscriptionName("my-signal").singleResult();

		LOGGER.info("execution = {}", execution);
		// 触发信号时和流程实例本身无关，只是发出信号
		runtimeService.signalEventReceived("my-signal");
		// 在触发了signalEventReceived后，流程实例执行完毕，在重新查询执行实例时返回为null
		execution = runtimeService.createExecutionQuery()
				.signalEventSubscriptionName("my-signal").singleResult();
		LOGGER.info("execution = {}", execution);
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-message-received.bpmn20.xml"})
	public void testMessageEventReceived() {
		LOGGER.info("测试触发流程中的消息捕获事件 messageCatchEvent");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		LOGGER.info("processInstance = {}", processInstance);
	
		Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();
		LOGGER.info("execution = {}", execution);
		// 发出message信号
		runtimeService.messageEventReceived("my-message", execution.getId());
	
		// 在触发了ReceiveTask后，流程实例执行完毕，在重新查询执行实例时返回为null
		execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();;
		LOGGER.info("execution = {}", execution);
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-message.bpmn20.xml"})
	public void testMessageStart() {
		LOGGER.info("测试基于Message启动的流程 messageStartEvent");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByMessage("my-message");
		LOGGER.info("processInstance = {}", processInstance);
	
		// 流程执行流实例已经结束
//		Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();
//		LOGGER.info("execution = {}", execution);
	}
	 
}
