package com.imooc.activiti.coreapi;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试 RepositoryService
 * 
 * @author zhangyan_g
 *
 */
public class RepositoryServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void testRepository() {
		LOGGER.info("testRepository");
		RepositoryService repositoryService = activitiRule.getRepositoryService();

		DeploymentBuilder deploymentment1 = repositoryService.createDeployment();
		deploymentment1.name("测试部署资源1")
				.addClasspathResource("my-process.bpmn20.xml")
				.addClasspathResource("second_approve.bpmn");
		
		// deploy操作完成后，会把部署对象 deploy，以及两个流程定义资源文件存储到数据库里
		Deployment deploy = deploymentment1.deploy();
		LOGGER.info("deploy = {}", deploy);

		// 模拟相同流程多次部署
		DeploymentBuilder deploymentment2 = repositoryService.createDeployment();
		deploymentment2.name("测试部署资源2")
				.addClasspathResource("my-process.bpmn20.xml")
				.addClasspathResource("second_approve.bpmn");
		deploymentment2.deploy();
		
		// 从数据库中查询 Deploy 数据
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		/*
		 * Deployment deployment = deploymentQuery
		 * 	.deploymentId(deploy.getId())
		 *  .singleResult();
		 * List<ProcessDefinition> definitionList = repositoryService
		 *	.createProcessDefinitionQuery()
		 *	.deploymentId(deployment.getId())
		 *	.listPage(0, 100);
		 */
		List<Deployment> deploymentList = deploymentQuery
				.orderByDeploymenTime().asc()
				.listPage(0, 100);
		/* 两个DeploymentEntity的ID分别是1和7，中间差了6个ID
		 * 一次部署会生成6个文件，对应于6个ID，
		 * 分别是 一个部署文件，两个流程定义文件，对应的两个流程定义文件的流数据，还有second_approve.bpmn所对应的图片
		 */
		for (Deployment deployment : deploymentList) {
			LOGGER.info("deployment = {}", deployment);
		}
		LOGGER.info("deploymentList.size = {}", deploymentList.size());
		
		List<ProcessDefinition> definitionList = repositoryService
				.createProcessDefinitionQuery()
//				.deploymentId(deployment.getId())
				.orderByProcessDefinitionKey().asc()
				.listPage(0, 100);
		for (ProcessDefinition processDefinition : definitionList) {
			LOGGER.info("processDefinition = {}, version = {}, key = {}, id = {}",
					processDefinition, 
					processDefinition.getVersion(),
					processDefinition.getKey(),
					processDefinition.getId());
		}
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testSuspend() {
		LOGGER.info("测试挂起流程后再启动");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().singleResult();
		LOGGER.info("processDefinition.id = {}", processDefinition.getId());

		/*
		 * 流程被挂起后，必须先激活才能再启动，否则会抛出ActivitiException异常
		 */
		LOGGER.info("挂起流程");
		repositoryService.suspendProcessDefinitionById(processDefinition.getId());
		try {
			LOGGER.info("开始启动");
			activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
			LOGGER.info("启动成功");
		} catch (Exception e) {
			LOGGER.info("启动失败");
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("激活流程");
		repositoryService.activateProcessDefinitionById(processDefinition.getId());
		LOGGER.info("开始启动");
		activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
		LOGGER.info("启动成功");
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
	public void testCandidateStarter() {
		LOGGER.info("测试用户组启动流程权限");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().singleResult();
		
		LOGGER.info("processDefinition.id = {}", processDefinition.getId());

		LOGGER.info("为流程存储服务添加候选用户和用户组");
		repositoryService.addCandidateStarterUser(processDefinition.getId(), "user");
		repositoryService.addCandidateStarterGroup(processDefinition.getId(), "groupM");

		LOGGER.info("获取流程存储服务中的候选用户和用户组");
		List<IdentityLink> identityLinkList = repositoryService
				.getIdentityLinksForProcessDefinition(processDefinition.getId());
		for (IdentityLink identityLink : identityLinkList) {
			LOGGER.info("identityLink = {}", identityLink);
		}
		
		LOGGER.info("从流程存储服务中删除候选用户和用户组");
		repositoryService.deleteCandidateStarterUser(processDefinition.getId(), "user");
		repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "groupM");
	}
}
