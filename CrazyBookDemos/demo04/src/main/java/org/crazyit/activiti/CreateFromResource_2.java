package org.crazyit.activiti;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 使用CreateProcessEngineConfigurationFromResource方法创建ProcessEngineConfiguration实例
 * 
 * 从classpath中读取自定义的 流程引擎配置文件 my-activiti2.xml，bean ID要求是 test2，但因没有这个bean而报错
 * @author Administrator
 * 
 */
public class CreateFromResource_2 {

	public static void main(String[] args) {
		// 指定配置文件创建ProcessEngineConfiguration
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(
						"my-activiti2.xml", "test");
		System.out.println(config.getProcessEngineName());
		// 因bean不存在而报错
		ProcessEngineConfiguration config2 = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(
						"my-activiti2.xml", "test2");
		System.out.println(config2.getProcessEngineName());
	}

}
