package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 测试
 * 1.
 * 2.
 * 3.
 * 4.
 */
public class GatewayTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试单一网关
     */
    @Test
    @Deployment(resources = {"my-process-exclusiveGateway1.bpmn20.xml"})
    public void testExclusiveGateway1() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 91);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-exclusiveGateway1.bpmn20.xml"})
    public void testExclusiveGateway2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 70);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());
    }

    /**
     * 测试
     */
    @Test
    @Deployment(resources = {"my-process-parallelGateway1.bpmn20.xml"})
    public void testParalleleGateway1() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        List<Task> taskList = activitiRule.getTaskService().createTaskQuery()
                .processInstanceId(processInstance.getId()).listPage(0, 100);
        LOGGER.info("当前的Task数量 taskList.size = {}", taskList.size());
        for (Task task : taskList) {
            LOGGER.info("取出当前 UserTask task.name = {}", task.getName());
            activitiRule.getTaskService().complete(task.getId());
        }

        // 执行了并行的 UserTask 之后
        taskList = activitiRule.getTaskService().createTaskQuery()
                .processInstanceId(processInstance.getId()).listPage(0, 100);
        LOGGER.info("执行了并行的 UserTask 之后，当前的Task数量 taskList.size = {}", taskList.size());
        for (Task task : taskList) {
            LOGGER.info("执行了并行的 UserTask 之后，取出当前 UserTask task.name = {}", task.getName());
        }
    }
}
