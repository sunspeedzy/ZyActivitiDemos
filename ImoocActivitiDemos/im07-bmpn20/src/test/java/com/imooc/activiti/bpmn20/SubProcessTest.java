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
 * 测试 普通子流程、事件子流程、调用式子流程
 */
public class SubProcessTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubProcessTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试子流程，使其停留在 订单完成 的UserTask节点
     */
    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());
    }

    /**
     * 测试子流程，添加启动参数，使其停留在 异常处理 的UserTask节点
     */
    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorflag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());
        //  执行结果 {parentVariable=parentValue, errorflag=true, variable=value}
        Map<String, Object> variables2 = activitiRule.getRuntimeService()
                .getVariables(processInstance.getId());
        LOGGER.info("取出当前流程的 变量 variables2 = {}", variables2);

        //  执行结果 {parentVariable=parentValue, errorflag=true, variable=value}
        Map<String, Object> localVar = activitiRule.getRuntimeService()
                .getVariablesLocal(processInstance.getId());
        LOGGER.info("取出当前流程的 Local变量 localVar = {}", localVar);
    }

    /**
     * 测试 事件子流程，添加启动参数，使其停留在 异常处理 的UserTask节点
     */
    @Test
    @Deployment(resources = {"my-process-subprocess2.bpmn20.xml"})
    public void testSubProcess3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorflag", true);
        variables.put("key1", "value1");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());

        // 执行结果 {key1=value1, key2=value2, parentVariable=parentValue, variable=value, errorflag=true}
        Map<String, Object> variables2 = activitiRule.getRuntimeService()
                .getVariables(processInstance.getId());
        LOGGER.info("取出当前流程的 变量 variables2 = {}", variables2);
        //  执行结果 {key1=value1, key2=value2, parentVariable=parentValue, variable=value, errorflag=true}
        Map<String, Object> localVar = activitiRule.getRuntimeService()
                .getVariablesLocal(processInstance.getId());
        LOGGER.info("取出当前流程的 Local变量 localVar = {}", localVar);
    }

    /**
     * 测试 调用式子流程，添加启动参数，使其停留在 异常处理 的UserTask节点
     */
    @Test
    @Deployment(resources = {"my-process-subprocess3.bpmn20.xml",
            "my-process-subprocess4.bpmn20.xml"})
    public void testSubProcess4() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorflag", false);
        variables.put("key0", "value0");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        LOGGER.info("取出当前UserTask task.name = {}", task.getName());

        // 执行结果 {key1=value1, key2=value2, parentVariable=parentValue, variable=value, errorflag=true}
        Map<String, Object> variables2 = activitiRule.getRuntimeService()
                .getVariables(processInstance.getId());
        LOGGER.info("取出当前流程的 变量 variables2 = {}", variables2);
        //  执行结果 {key1=value1, key2=value2, parentVariable=parentValue, variable=value, errorflag=true}
        Map<String, Object> localVar = activitiRule.getRuntimeService()
                .getVariablesLocal(processInstance.getId());
        LOGGER.info("取出当前流程的 Local变量 localVar = {}", localVar);
    }
}
