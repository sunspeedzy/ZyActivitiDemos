package org.crazyit.activiti;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 使用createStandaloneInMemProcessEngineConfiguration创建ProcessEngineConfiguration
 * 
 * 创建ProcessEngineConfiguration时，不会读取任何Activiti配置文件，流程引擎配置全部使用默认值
 * 
 * @author yangenxiong
 * 
 */
public class CreateStandaloneInMem {

	public static void main(String[] args) {
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();
		// 值为create-drop
		System.out.println(config.getDatabaseSchemaUpdate());
		// 值为jdbc:h2:mem:activiti
		System.out.println(config.getJdbcUrl());
	}

}
