package com.imooc.activiti.coreapi;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.imooc.activiti.mapper.MyCustomMapper;


/**
 * 测试 FormService
 * 
 * @author zhangyan_g
 *
 */
public class ManagementServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ManagementServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");
	
	@Test
	@Deployment(resources = {"my-process-job.bpmn20.xml"})
	public void testJobQuery() {
		LOGGER.info("测试Job查询");
		ManagementService managementService = activitiRule.getManagementService();
		List<Job> timerJobList = managementService.createTimerJobQuery().listPage(0, 100);
		for (Job timerJob : timerJobList) {
			LOGGER.info("timerJob = {}", timerJob);
		}
		
		JobQuery jobQuery = managementService.createJobQuery();
		SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
		// dead letter job需要运维人员关注，查看未能执行下去的原因
		DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
	}

	@Test
	@Deployment(resources = {"my-process-job.bpmn20.xml"})
	public void testTablePageQuery() {
		LOGGER.info("测试通用表查询");
		ManagementService managementService = activitiRule.getManagementService();
		TablePage tablePage = managementService.createTablePageQuery()
				.tableName(managementService.getTableName(ProcessDefinition.class))
				.listPage(0, 100);
		List<Map<String,Object>> rows = tablePage.getRows();
		for (Map<String, Object> row : rows) {
			LOGGER.info("row = {}", row);
		}
	}

	@Test
	@Deployment(resources = {"my-process.bpmn20.xml"})
	public void testCustomSqlQuery() {
		LOGGER.info("测试自定义SQL查询");
		activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		ManagementService managementService = activitiRule.getManagementService();
		List<Map<String, Object>> mapList = managementService
				.executeCustomSql(new AbstractCustomSqlExecution<MyCustomMapper, List<Map<String, Object>>>(MyCustomMapper.class) {
			@Override
			public List<Map<String, Object>> execute(MyCustomMapper mapper) {
				return mapper.findAll();
			}
		});
		for (Map<String, Object> map : mapList) {
			LOGGER.info("map = {}", map);
		}
	}

	@Test
	@Deployment(resources = {"my-process.bpmn20.xml"})
	public void testCommand() {
		LOGGER.info("测试命令执行");
		activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		ManagementService managementService = activitiRule.getManagementService();
		ProcessDefinitionEntity processDefinitionEntity = managementService
				.executeCommand(new Command<ProcessDefinitionEntity>() {
					@Override
					public ProcessDefinitionEntity execute(CommandContext commandContext) {
						// TODO Auto-generated method stub
						ProcessDefinitionEntity processDefinitionEntity = commandContext
								.getProcessDefinitionEntityManager().findLatestProcessDefinitionByKey("my-process");
						return processDefinitionEntity;
					}
				});
		
		LOGGER.info("processDefinitionEntity = {}", processDefinitionEntity);
	}
}
