package org.crazyit.activiti.custom.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

/**
 * 
 * @author zhangyan_g
 *
 */
public class MyConfig {

	public static void main(String[] args) {
		ProcessEngines.getDefaultProcessEngine();
		// 创建Activiti配置对象
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("my-config.xml", "testInterceptor");		
		// 初始化流程引擎
		ProcessEngine engine = config.buildProcessEngine();
		// 部署一个最简单的流程
		engine.getRepositoryService().createDeployment()
				.addClasspathResource("bpmn/config.bpmn20.xml").deploy();
		// 构建流程参数
		Map<String, Object> vars = new HashMap<String, Object>(8);
		vars.put("day", 10);
		// 开始流程
		engine.getRuntimeService().startProcessInstanceByKey("vacationProcess",
				vars);
		System.exit(0);
	}

}
