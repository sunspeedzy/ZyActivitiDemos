package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * 测试
 * 1. 自制JavaDelegate实现类提供的Service
 * 2. 自制ActivitiBehavior实现类提供的Service
 * 3. 自制JavaDelegate实现类提供的Service，测试注入属性
 */
public class ServiceTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试 自制JavaDelegate实现类提供的Service
     */
    @Test
    @Deployment(resources = {"my-process-servicetask1.bpmn20.xml"})
    public void testServiceTask1() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricActivityInstance> activityInstances =
                historyService.createHistoricActivityInstanceQuery()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .listPage(0, 100);
        for (HistoricActivityInstance activityInstance : activityInstances) {
            LOGGER.info("执行的Activiti = {}", activityInstance);
        }
        LOGGER.info("执行的Activiti的数量 = {}", activityInstances.size());
    }

    /**
     * 测试 自制ActivitiBehavior实现类提供的Service
     */
    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testScriptTask2() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricActivityInstance> activityInstances =
                historyService.createHistoricActivityInstanceQuery()
                        .orderByHistoricActivityInstanceId().asc()
                        .listPage(0, 100);
        for (HistoricActivityInstance activityInstance : activityInstances) {
            LOGGER.info("执行的Activiti = {}", activityInstance);
        }
        LOGGER.info("执行的Activiti的数量 = {}", activityInstances.size());
        // 执行到此，流程暂停了，因为ActivitiBehavior提供的Service会使流程等待

        // 查看执行实例
        Execution execution = activitiRule.getRuntimeService().createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        assertNotNull(execution);
        LOGGER.info("执行对象不为Null execution = {}", execution);

        // 驱动 ServiceTask 完成执行
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(commandContext -> {
            // ActivitiEngineAgenda 实例是手工执行流程节点的利器，所有节点在执行过程中都使用了这个实例执行
            ActivitiEngineAgenda agenda = commandContext.getAgenda();
            agenda.planTakeOutgoingSequenceFlowsOperation(
                    (ExecutionEntity) execution, false);
            return null;
        });
        // 查看执行的历史节点
        activityInstances = historyService.createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime().asc()
                .listPage(0, 100);
        for (HistoricActivityInstance activityInstance : activityInstances) {
            LOGGER.info("执行的Activiti = {}", activityInstance);
        }
        LOGGER.info("执行的Activiti的数量 = {}", activityInstances.size());
    }

    /**
     * 测试 自制JavaDelegate实现类提供的Service，测试注入属性
     */
    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc", "the test java delegate");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
    }
}
