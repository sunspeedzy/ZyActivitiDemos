package com.imooc.activiti.config;

import java.sql.SQLException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试数据库配置
 * @author zhangyan_g
 *
 */
public class ConfigDBTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);
	
	@Test
	public void testConfig1() throws SQLException {
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResourceDefault();
		
		LOGGER.info("testConfig1 configuration = {}", configuration);
		ProcessEngine processEngine = configuration.buildProcessEngine();
		LOGGER.info("testConfig1  获取流程引擎 {}", processEngine.getName());
		LOGGER.info("testConfig1  获取数据源 {}", 
				configuration.getDataSource().getConnection()
				.getMetaData().getDriverName());
		LOGGER.info("testConfig1  获取数据源类名 {}", 
				configuration.getDataSource().getClass().getName());
		processEngine.close();
	}
	
	@Test
	public void testConfig2() throws SQLException {
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");

		LOGGER.info("testConfig2 configuration = {}", configuration);
		ProcessEngine processEngine = configuration.buildProcessEngine();
		LOGGER.info("testConfig2  获取流程引擎 {}", processEngine.getName());
		LOGGER.info("testConfig2  获取数据源 {}", 
				configuration.getDataSource().getConnection()
				.getMetaData().getDriverName());
		LOGGER.info("testConfig2  获取数据源类名 {}", 
				configuration.getDataSource().getClass().getName());
		processEngine.close();
	}
}
