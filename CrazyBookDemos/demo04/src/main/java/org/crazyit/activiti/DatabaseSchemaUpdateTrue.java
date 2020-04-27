package org.crazyit.activiti;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 将databaseSchemaUpdate设置为true后，调用config.buildProcessEngine() 时，
 * Activiti会对数据库中所有的表进行更新，如果表不存在则Activiti会自动创建（开发时常用）
 * 
 * @author zhangyan_g
 *
 */
public class DatabaseSchemaUpdateTrue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//读取Activiti配置
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("schema_update/schemaUpdate-true.xml");
		System.out.println(config.getClass().getName());
		//启动Activiti
		config.buildProcessEngine();
	}

}
