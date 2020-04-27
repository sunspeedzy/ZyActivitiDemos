package org.crazyit.activiti;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 将databaseSchemaUpdate设置为false（默认值）后，调用config.buildProcessEngine() 时，
 * activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配，将抛出异常。（生产环境常用）
 * 
 * @author zhangyan_g
 *
 */
public class DatabaseSchemaUpdateFalse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//读取Activiti配置
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("schema_update/schemaUpdate-false.xml");
		//启动Activiti
		config.buildProcessEngine();
	}

}
