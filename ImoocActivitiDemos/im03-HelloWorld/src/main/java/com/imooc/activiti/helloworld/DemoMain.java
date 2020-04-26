package com.imooc.activiti.helloworld;

import java.util.List;
import java.util.Scanner;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动类
 * 
 * @author zhangyan
 * @create 2020-04-25
 */
public class DemoMain {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);
	
	public static void main(String[] args) {
		LOGGER.info("启动我们的程序");
		// 创建流程引擎
		ProcessEngine processEngine = getProcessEngine();
		
		// 部署流程定义文件
		ProcessDefinition processDefinition = getProcessDefinition(processEngine);
		
		// 启动运行流程
		ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);
		// 处理流程任务
		Scanner scanner = new Scanner(System.in);
		while (processInstance != null) {
			
		}
		TaskService taskService = processEngine.getTaskService();
		List<Task> list = taskService.createTaskQuery().list();
		for (Task task : list) {
			LOGGER.info("待处理任务 [{}]", task.getName());
		}
		LOGGER.info("待处理任务数量 [{}]", list.size());
		
		LOGGER.info("结束我们的程序");
	}

	private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		LOGGER.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());
		return processInstance;
	}

	private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("diagrams/second_approve.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		String deploymentId = deployment.getId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.deploymentId(deploymentId)
			.singleResult();
		LOGGER.info("流程定义文件 [{}]，流程ID [{}]", processDefinition.getName(), processDefinition.getId());
		return processDefinition;
	}
	
	/**
	 * 创建流程引擎
	 * 
	 * @return
	 */
	private static ProcessEngine getProcessEngine() {
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		ProcessEngine processEngine = cfg.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		LOGGER.info("流程引擎名称 {}, 版本 {}", name, version);
		return processEngine;
	}

}
