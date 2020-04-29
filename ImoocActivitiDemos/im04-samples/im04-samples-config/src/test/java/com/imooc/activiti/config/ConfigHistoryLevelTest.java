package com.imooc.activiti.config;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
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
 * MDC 测试
 * 
 * @author zhangyan_g
 *
 */
public class ConfigHistoryLevelTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process.bpmn20.xml"})
	public void test() {
		//启动流程
		startProcessInstance();
		//修改变量
		changeVariable();
		
		//提交表单 task
		submitTaskFormData();

		//输出历史内容
		//输出历史活动
		showHistoryActiviti();

		//输出历史变量
		showHistoryVariable();
		//输出历史用户任务
		showHistoryTask();
		//输出历史表单的详情
		showHistoryForm();
		//输出历史详情
		showHistoryDetail();
	}

	private void showHistoryDetail() {
		LOGGER.info("查看 HistoricDetail（历史详情）信息");
		List<HistoricDetail> historicDetails = activitiRule
				.getHistoryService()
				.createHistoricDetailQuery()
				.listPage(0, 100);
		for (HistoricDetail historicDetail : historicDetails) {
			LOGGER.info("historicDetail = {}", toString(historicDetail));
		}
		LOGGER.info("historicDetails.size = {}", historicDetails.size());
	}

	private void showHistoryForm() {
		LOGGER.info("查看 HistoricDetail（历史表单详情）信息");
		List<HistoricDetail> historicDetailsForm = activitiRule
				.getHistoryService()
				.createHistoricDetailQuery()
				.formProperties()
				.listPage(0, 100);
		for (HistoricDetail historicDetail : historicDetailsForm) {
			LOGGER.info("historicDetail = {}", toString(historicDetail));
		}
		LOGGER.info("historicDetailsForm.size = {}", historicDetailsForm.size());
	}

	private void showHistoryTask() {
		LOGGER.info("查看 HistoricTaskInstance（历史用户任务）信息");
		List<HistoricTaskInstance> historicTaskInstances = activitiRule
				.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.listPage(0, 100);
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			LOGGER.info("historicTaskInstance = {}", toString(historicTaskInstance));
		}
		LOGGER.info("historicTaskInstances.size = {}", historicTaskInstances.size());
	}

	private void showHistoryVariable() {
		List<HistoricVariableInstance> historicVariableInstances =
				activitiRule.getHistoryService()
				.createHistoricVariableInstanceQuery()
				.listPage(0, 100);
		LOGGER.info("查看 HistoricVariableInstance（历史变量）信息");
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			LOGGER.info("historicVariableInstance = {}", historicVariableInstance);
		}
		LOGGER.info("historicVariableInstances.size = {}", historicVariableInstances.size());
	}

	private void showHistoryActiviti() {
		LOGGER.info("查看 HistoricActivityInstance（历史活动）信息");
		List<HistoricActivityInstance> historicActivityInstances =
				activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
		}
		LOGGER.info("historicActivityInstances.size = {}", historicActivityInstances.size());
	}

	private void submitTaskFormData() {
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		// 动态传入表单参数
		// audit HistoryLevel才会记录表单参数信息
		Map<String, String> properties = Maps.newHashMap();
		properties.put("formKey1", "valuef1");
		properties.put("formKey2", "valuef2");
		activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
	}

	private void changeVariable() {
		// 参数传入、后启动完成后，在任务节点会产生一次停顿，可以在停顿时发起修改变量的操作
		// 分页查询
		List<Execution> executions = activitiRule
				.getRuntimeService()
				.createExecutionQuery().listPage(0, 100);
		LOGGER.info("查看Execution信息");
		for (Execution execution : executions) {
			LOGGER.info("execution = {}", execution);
		}
		LOGGER.info("executions.size = {}", executions.size());
		String id = executions.iterator().next().getId();
		activitiRule.getRuntimeService().setVariable(id, "keyStart1", "value1_");
	}

	private void startProcessInstance() {
		// 定义Map作为启动参数
		Map<String, Object> params = Maps.newHashMap();
		params.put("keyStart1", "value1");
		params.put("keyStart2", "value2");
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("my-process", params);
	}
	
	private String toString(HistoricTaskInstance historicTaskInstance) {
		return ToStringBuilder.reflectionToString(
				historicTaskInstance, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * 将 历史详情 实例的信息拼成字符串
	 * @param historicDetail
	 * @return
	 */
	private String toString(HistoricDetail historicDetail) {
		return ToStringBuilder.reflectionToString(
				historicDetail, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
