package org.crazyit.activiti;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 使用CreateProcessEngineConfigurationFromResource方法创建ProcessEngineConfiguration实例
 * 
 * 从classpath中读取自定义的 流程引擎配置文件 my-activiti1.xml，bean ID为默认的processEngineConfiguration
 * @author Administrator
 * 
 */
public class CreateFromResource_1 {

	public static void main(String[] args) {
		// 指定配置文件创建ProcessEngineConfiguration
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("my-activiti1.xml");

	}

}
