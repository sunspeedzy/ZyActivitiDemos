package com.imooc.activiti.coreapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;


/**
 * 测试 TaskService
 * 
 * @author zhangyan_g
 *
 */
public class TaskServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
	public void testTaskService() {
		LOGGER.info("测试任务管理服务，Task管理和流程控制");
		// 构建参数变量
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("message", "my test message!!!");
		// 启动流程
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		runtimeService.startProcessInstanceByKey("my-process", variables);
		// 对TaskService进行操作
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		LOGGER.info("Task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
		LOGGER.info("Task.description = {}", task.getDescription());
		
		// 为TaskService设置普通变量
		taskService.setVariable(task.getId(), "key1", "value1");
		// 为TaskService设置本地变量
		taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");
		// 获取变量
		Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
		Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
		// 无法获取TaskService的local变量
		Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
		LOGGER.info("taskServiceVariables = {}", taskServiceVariables);
		LOGGER.info("taskServiceVariablesLocal = {}", taskServiceVariablesLocal);
		LOGGER.info("processVariables = {}", processVariables);
		// 驱动完成TaskService
		HashMap<String, Object> completeVar = Maps.newHashMap();
		completeVar.put("cKey1", "cValue1");
		taskService.complete(task.getId(), completeVar);
		// 驱动完成TaskService后，TaskService被自动删除
		Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
		LOGGER.info("task1 = {}", task1);
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
	public void testTaskServiceUser() {
		LOGGER.info("测试任务管理服务，设置Task权限信息");
		// 构建参数变量
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("message", "my test message!!!");
		// 启动流程
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		runtimeService.startProcessInstanceByKey("my-process", variables);
		// 对TaskService进行操作
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		LOGGER.info("Task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
		LOGGER.info("Task.description = {}", task.getDescription());
		
		// 设置Owner
		taskService.setOwner(task.getId(), "user1");
		// 设置代办人，但是这种方法会强制原有代办人被取消代办资格，不推荐使用
//		taskService.setAssignee(task.getId(), "jimmy");
		/*
		 * 查找候选人里有这个代办人、但还没有指定代办人的任务
		 */
		List<Task> taskList = taskService.createTaskQuery()
				.taskCandidateUser("jimmy")
				.taskUnassigned().listPage(0, 100);
		for (Task task2 : taskList) {
			try {
				// 当任务已有代办人时，claim方法会抛出异常
				taskService.claim(task2.getId(), "jimmy");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		// 构建了这个任务和用户与组的关系
		List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
		for (IdentityLink identityLink : identityLinksForTask) {
			LOGGER.info("identityLink = {}", identityLink);
		}
		// 获取代办人是jimmy的task
		List<Task> jimmys = taskService.createTaskQuery()
				.taskAssignee("jimmy").listPage(0, 100);
		for (Task jimmy : jimmys) {
			HashMap<String, Object> vars = Maps.newHashMap();
			vars.put("cKey1", "cValue1");
			taskService.complete(jimmy.getId(), vars);
		}
		// 驱动完成TaskService后，TaskService被自动删除
		jimmys = taskService.createTaskQuery()
				.taskAssignee("jimmy").listPage(0, 100);
		LOGGER.info("是否存在 {}", CollectionUtils.isEmpty(jimmys));
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
	public void testTaskAttachment() {
		LOGGER.info("测试任务管理服务，创建与查询Attachment");
		// 构建参数变量
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("message", "my test message!!!");
		// 启动流程
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		runtimeService.startProcessInstanceByKey("my-process", variables);
		// 对TaskService进行操作
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		taskService.createAttachment("url", task.getId(),
				task.getProcessInstanceId(), "name", 
				"desc", "/url/test.png");
		List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
		for (Attachment taskAttachment : taskAttachments) {
			LOGGER.info("taskAttachment = {}", ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE));
		}
	}

	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
	public void testTaskComment() {
		LOGGER.info("测试任务管理服务，创建与查询Comment");
		// 构建参数变量
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("message", "my test message!!!");
		// 启动流程
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		runtimeService.startProcessInstanceByKey("my-process", variables);
		// 对TaskService进行操作
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		
		taskService.setOwner(task.getId(), "user1");
		taskService.setAssignee(task.getId(), "jimmy");
		taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note 1");
		taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note 2");
		
		List<Comment> taskComments = taskService.getTaskComments(task.getId());
		for (Comment taskComment : taskComments) {
			LOGGER.info("taskComment = {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
		}
		
		List<Event> taskEvents = taskService.getTaskEvents(task.getId());
		for (Event event : taskEvents) {
			LOGGER.info("event = {}", ToStringBuilder.reflectionToString(event, ToStringStyle.JSON_STYLE));
		}
	}
}
