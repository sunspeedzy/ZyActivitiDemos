package com.imooc.activiti.dbentity;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示内容：
 * 补充act_re_procdef表中的字段
 * 
 * @author zhangyan
 *
 */
public class DbRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRepositoryTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
	
	@Test
	public void testDeploy() {
		LOGGER.info("部署流程定义文件，查看 ACT_RE_PROCDEF 表的内容");
		activitiRule.getRepositoryService().createDeployment()
				.name("二次审批流程")
				.addClasspathResource("second_approve.bpmn20.xml")
				.deploy();
	}

	@Test
	public void testSuspend() {
		LOGGER.info("部署流程定义文件，查看 ACT_RE_PROCDEF 表的内容中的 挂起状态 字段");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.suspendProcessDefinitionById("second_approve:1:5004");
		boolean suspended = repositoryService.isProcessDefinitionSuspended("second_approve:1:5004");
		LOGGER.info("suspended = {}", suspended);
	}
	
}
