package org.crazyit.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
/**
 * 配置文件database-type.xml中 databaseType的数据库类型值与JDBC串中的不符，使用了错误的SQL语句建表
 * 导致抛出异常 com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException
 * 
 * @author zhangyan_g
 *
 */
public class DatabaseType {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 读取Activiti配置
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("database-type.xml");
		// 启动Activiti
		ProcessEngine engine = config.buildProcessEngine();
		// 关闭流程引擎
		engine.close();
	}

}
