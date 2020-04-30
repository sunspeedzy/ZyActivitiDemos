package com.imooc.activiti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 启动类
 * 
 * 启动方式：
 * 1. 在Eclipse里直接运行该程序
 * 2. 在 ZyActivitDemos的pom.xml 和 im03-HelloWorld的pom.xml 中配置了 spring-boot-starter 后，
 *    在 im03-HelloWorld 目录下使用 mvn spring-boot:run 的命令启动
 * 
 * @author zhangyan
 * @create 2020-04-25
 */
public class DemoMain {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

	public static void main(String[] args) throws ParseException {
		LOGGER.info("启动我们的程序");
		// 创建流程引擎
		ProcessEngine processEngine = getProcessEngine();

		// 部署流程定义文件
		ProcessDefinition processDefinition = getProcessDefinition(processEngine);

		// 启动运行流程
		ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);
		// 处理流程任务
		processTask(processEngine, processInstance);

		// 关闭流程引擎
		// 如果数据库配置策略是 create-drop，则必须调用此方法，否则不会drop表
		processEngine.close();
		LOGGER.info("结束我们的程序");
	}

	private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance)
			throws ParseException {
		Scanner scanner = new Scanner(System.in);
		while (processInstance != null && !processInstance.isEnded()) {
			TaskService taskService = processEngine.getTaskService();
			List<Task> list = taskService.createTaskQuery().list();
			LOGGER.info("待处理任务数量 [{}]", list.size());
			for (Task task : list) {
				LOGGER.info("待处理任务 [{}]", task.getName());
				Map<String, Object> variables = getMap(processEngine, scanner, task);
				
				taskService.complete(task.getId(), variables);
				processInstance = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstance.getId())
						.singleResult();
			}
		}
		scanner.close();
	}

	private static Map<String, Object> getMap(ProcessEngine processEngine, Scanner scanner, Task task)
			throws ParseException {
		FormService formService = processEngine.getFormService();
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		Map<String, Object> variables = Maps.newHashMap();
		for (FormProperty property : formProperties) {
			String line = null;
			if (StringFormType.class.isInstance(property.getType())) {
				LOGGER.info("请输入 {} ？", property.getName());
				line = scanner.nextLine();
				variables.put(property.getId(), line);
			} else if (DateFormType.class.isInstance(property.getType())) {
				LOGGER.info("请输入 {} ？格式 （yyyy-MM-dd）", property.getName());
				line = scanner.nextLine();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(line);
				variables.put(property.getId(), date);
			} else {
				LOGGER.info("类型暂不支持 {}", property.getType());
			}
			LOGGER.info("您输入的内容是 [{}]", line);

		}
		return variables;
	}

	private static ProcessInstance getProcessInstance(ProcessEngine processEngine,
			ProcessDefinition processDefinition) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		LOGGER.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());
		return processInstance;
	}

	private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("second_approve.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		String deploymentId = deployment.getId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId).singleResult();
		LOGGER.info("流程定义文件 [{}]，流程ID [{}]", processDefinition.getName(), processDefinition.getId());
		return processDefinition;
	}

	/**
	 * 创建流程引擎
	 * 
	 * @return
	 */
	private static ProcessEngine getProcessEngine() {
		// 创建流程引擎配置，不读取任何Activiti配置文件，流程引擎配置的全部属性都是用默认值
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		// 启动Activiti
		// 调用此方法创建流程引擎，这时Activiti的数据库表才会按照配置的策略进行创建
		ProcessEngine processEngine = cfg.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		LOGGER.info("流程引擎名称 {}, 版本 {}", name, version);
		return processEngine;
	}

}
