package org.crazyit.activiti;

import org.activiti.engine.ProcessEngine;

import org.activiti.engine.ProcessEngineConfiguration;
/**
 * 将databaseSchemaUpdate设置为 create-drop 后，
 * 在activiti启动时（调用config.buildProcessEngine() 时）创建表，
 * 在关闭时删除表（必须手动关闭引擎，才能删除表）。（单元测试常用）
 * @author zhangyan_g
 *
 */
public class DatabaseSchemaUpdateCreateDrop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 读取Activiti配置
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("schema_update/schemaUpdate-create-drop.xml");
		// 启动Activiti
		/* 执行了org/activiti/db/create/activiti.mysql.create.engine.sql、
		 *    activiti.mysql.create.history.sql、
		 *    activiti.mysql.create.identity.sql */
		ProcessEngine engine = config.buildProcessEngine();
		System.out.println("流程引擎创建完毕");
		System.out.println("流程引擎即将关闭...");
		// 关闭流程引擎
		/*
		 * 执行了 org/activiti/db/drop/activiti.mysql.drop.engine.sql、
		 * activiti.mysql.drop.history.sql、
		 * activiti.mysql.drop.identity.sql
		 */
		engine.close();
	}

}
