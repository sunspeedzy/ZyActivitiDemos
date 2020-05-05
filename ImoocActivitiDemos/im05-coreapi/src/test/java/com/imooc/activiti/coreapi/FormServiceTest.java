package com.imooc.activiti.coreapi;


import java.util.HashMap;
import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;


/**
 * 测试 FormService
 * 
 * @author zhangyan_g
 *
 */
public class FormServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(FormServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"my-process-form.bpmn"})
	public void testIdentityService() {
		LOGGER.info("测试FormService表单服务，在startevent里配置表单项");
		// 获取FormService实例和流程定义ID
		FormService formService = activitiRule.getFormService();
		ProcessDefinition processDefinition = activitiRule.getRepositoryService()
				.createProcessDefinitionQuery().singleResult();
		
		// 通过流程定义文件ID获取startevent中的表单项的key，即StartFormKey
		String startFormKey = formService.getStartFormKey(processDefinition.getId());
		LOGGER.info("startFormKey = {}", startFormKey);

		// 通过流程定义文件ID获取startevent中的表单项的data，即StartFormData
		StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
		List<FormProperty> startformProperties = startFormData.getFormProperties();
		for (FormProperty startFormProperty : startformProperties) {
			LOGGER.info("startFormProperty = {}", 
					ToStringBuilder.reflectionToString(startFormProperty, ToStringStyle.JSON_STYLE));
		}
		
		// 传入startFormData参数值，启动流程实例
		HashMap<String, String> properties = Maps.newHashMap();
		properties.put("message", "my test message");
		formService.submitStartFormData(processDefinition.getId(), properties);
		
		// 获取 Task 的表单项
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormProperty> taskFormDataFormProperties = taskFormData.getFormProperties();
		for (FormProperty taskFormProperty : taskFormDataFormProperties) {
			LOGGER.info("taskFormProperty = {}", 
					ToStringBuilder.reflectionToString(taskFormProperty, ToStringStyle.JSON_STYLE));
		}
		// 传入task表单项的值，提交表单执行Task
		HashMap<String, String> formProperties = Maps.newHashMap();
		formProperties.put("yesOrNo", "yes");
		formService.submitTaskFormData(task.getId(), formProperties);
		
		Task task2 = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
		LOGGER.info("task2 = {}", task2);
	}
	
}
