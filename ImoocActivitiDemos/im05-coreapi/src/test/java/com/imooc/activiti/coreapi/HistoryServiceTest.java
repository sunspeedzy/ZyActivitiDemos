package com.imooc.activiti.coreapi;


import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.ProcessInstanceHistoryLog;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
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
 * 测试 HistoryService
 * 
 * @author zhangyan_g
 *
 */
public class HistoryServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(HistoryServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");
	
	@Test
	@Deployment(resources = {"my-process.bpmn20.xml"})
	public void testHistoryService() {
		LOGGER.info("测试HistoryService历史服务");
		// 获取HistoryService
		HistoryService historyService = activitiRule.getHistoryService();
		/* 
		 * 启动流程实例，这样才能产生历史数据
		 * 普通变量可以持久化到数据库，而瞬时变量不能被持久化到数据库
		 * 所以采用 ProcessInstanceBuilder来启动流程实例
		 */
		ProcessInstanceBuilder processInstanceBuilder = activitiRule
				.getRuntimeService().createProcessInstanceBuilder();
		// 普通变量
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("key0", "value0");
		variables.put("key1", "value1");
		variables.put("key2", "value2");
		// 瞬时变量
		Map<String, Object> transientVariables = Maps.newHashMap();
		transientVariables.put("tkey1", "tvalue1");
		
		ProcessInstance processInstance = processInstanceBuilder
				.processDefinitionKey("my-process")
				.variables(variables)
				.transientVariables(transientVariables)
				.start();
		// 通过RuntimeService直接修改变量
		activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value1_1");
		
		// 经过表单存储的数据也会存储到HistoricDetail里面
		Task task = activitiRule.getTaskService()
				.createTaskQuery()
				.processInstanceId(processInstance.getId())
				.singleResult();
		//		activitiRule.getTaskService().complete(task.getId(), variables, transientVariables);
		Map<String, String> properties = Maps.newHashMap();
		properties.put("fKey1", "fValue1");
		// 提交表单时覆盖原有变量
		properties.put("key2", "value_2_2");
		activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
		
		List<HistoricProcessInstance> historicProcessInstances = 
				historyService.createHistoricProcessInstanceQuery().listPage(0, 100);
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			LOGGER.info("historicProcessInstance = {}", 
					ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
		}
		
		List<HistoricActivityInstance> historicActivityInstances =
				historyService.createHistoricActivityInstanceQuery().listPage(0, 100);
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
		}
		
		List<HistoricTaskInstance> historicTaskInstances =
				historyService.createHistoricTaskInstanceQuery().listPage(0, 100);
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			LOGGER.info("historicTaskInstance = {}", 
					ToStringBuilder.reflectionToString(historicTaskInstance, ToStringStyle.JSON_STYLE));
		}
		
		List<HistoricVariableInstance> historicVariableInstances =
				historyService.createHistoricVariableInstanceQuery().listPage(0, 100);
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			LOGGER.info("historicVariableInstance = {}", historicVariableInstance);
		}
		
		List<HistoricDetail> historicDetails =
				historyService.createHistoricDetailQuery().listPage(0, 100);
		for (HistoricDetail historicDetail : historicDetails) {
			LOGGER.info("historicDetail = {}", 
					ToStringBuilder.reflectionToString(historicDetail, ToStringStyle.JSON_STYLE));
		}
		// 获取流程实例历史日志信息
		ProcessInstanceHistoryLog processInstanceHistoryLog = historyService
				.createProcessInstanceHistoryLogQuery(processInstance.getId())
				.includeVariables()
				.includeFormProperties()
				.includeComments()
				.includeTasks()
				.includeActivities()
				.includeComments()
				.includeVariableUpdates()
				.singleResult();
		List<HistoricData> historicDataList = processInstanceHistoryLog.getHistoricData();
		for (HistoricData historicData : historicDataList) {
			LOGGER.info("historicData = {}", 
					ToStringBuilder.reflectionToString(historicData, ToStringStyle.JSON_STYLE));
		}
		// 删除历史流程实例，用以验证删除后无法获取到
		historyService.deleteHistoricProcessInstance(processInstance.getId());
		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstance.getId())
				.singleResult();
		LOGGER.info("historicProcessInstance = {}", historicProcessInstance);
	}
	
}
