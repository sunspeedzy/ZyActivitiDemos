package com.imooc.activiti.dbentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 测试 DB 配置
 * 
 * @author zhangyan
 *
 */
public class DbConfigTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigTest.class);
	
	@Test
	public void testDbConfig() {
		LOGGER.info("演示历史数据表和身份数据表是可配置的，需要先清空MySQL数据库里的activiti表");
		ProcessEngine processEngine = ProcessEngineConfiguration
//				.createProcessEngineConfigurationFromResourceDefault()
				.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
				.buildProcessEngine();
		ManagementService managementService = processEngine.getManagementService();
		Map<String, Long> tableCount = managementService.getTableCount();
		ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
		Collections.sort(tableNames);
		for (String tableName : tableNames) {
			LOGGER.info("table = {}", tableName);
		}
		
		LOGGER.info("tableNames.size = {}", tableNames.size());
	}
	
	@Test
	public void testDropTable() {
		LOGGER.info("演示删除表结构");
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti-mysql-idhis.cfg.xml")
				.buildProcessEngine();
		ManagementService managementService = processEngine.getManagementService();
		managementService.executeCommand(new Command<Object>() {

			@Override
			public Object execute(CommandContext commandContext) {
				// 删除Schema
				commandContext.getDbSqlSession().dbSchemaDrop();
				LOGGER.info("删除表结构");
				return null;
			}
			
		});
	}
}
