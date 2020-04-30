package com.imooc.activiti.config;

import java.util.List;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imooc.activiti.event.CustomEventListenerAddedInCode;


/**
 * 作业执行器配置 测试
 * 
 * @author zhangyan_g
 *
 */
public class ConfigJobTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigJobTest.class);

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process_job.bpmn"})
	public void test() throws InterruptedException {
		// 因为流程文件部署后会定时执行，所以不需要在代码中启动流程了
		LOGGER.info("start");
		List<Job> jobList = activitiRule
				.getManagementService()
				.createTimerJobQuery().listPage(0, 100);
		for (Job job : jobList) {
			LOGGER.info("定时任务 = {}，默认重试次数 = {}", job, job.getRetries());
		}
		LOGGER.info("jobList.size = {}", jobList.size());
		Thread.sleep(100000);
		LOGGER.info("end");
	}
}
