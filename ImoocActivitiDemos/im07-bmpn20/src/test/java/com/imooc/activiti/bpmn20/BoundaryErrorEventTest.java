package com.imooc.activiti.bpmn20;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.activiti.engine.impl.test.TestHelper.assertProcessEnded;
import static org.junit.Assert.assertEquals;

public class BoundaryErrorEventTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryErrorEventTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
//    @Deployment(resources = { "reviewSalesLead.bpmn20.xml" })
    public void testReviewSalesLeadProcess() {
        LOGGER.info("部署流程定义文件 reviewSalesLead.bpmn20.xml");
        /* 使用 @Deployment(resources = { "reviewSalesLead.bpmn20.xml" }) 注解方式，
           在测试程序终止后，清空表中相关的数据
           而使用这种部署流程定义文件的方式，可以在测试程序终止后，在 act_hi_* 表中保留历史数据
         */
        activitiRule.getRepositoryService()
                .createDeployment().name("测试BoundaryErrorEvent")
                .addClasspathResource("reviewSalesLead.bpmn20.xml").deploy();

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        ProcessEngine processEngine = activitiRule.getProcessEngine();
        // Normally the UI will do this automatically for us
        // 设置当前用户为kermit
        Authentication.setAuthenticatedUserId("kermit");

        // After starting the process, a task should be assigned to the
        // 'initiator' (normally set by GUI)
        Map<String, Object> variables = new HashMap<>();
        variables.put("details", "very interesting");
        variables.put("customerName", "Alfresco");
        String procId = runtimeService.startProcessInstanceByKey("reviewSaledLead", variables).getId();
        Task task = taskService.createTaskQuery().taskAssignee("kermit").singleResult();
        assertEquals("Provide new sales lead", task.getName());
        LOGGER.info("输出 task.getName = {}", task.getName());

        // After completing the task, the review subprocess will be active
        // 未提供 Customer name、Potential profit、Details 变量，就提交了UserTask "Provide new sales lead"
        taskService.complete(task.getId());

        Task ratingTask = taskService.createTaskQuery().taskCandidateGroup("accountancy").singleResult();
        assertEquals("Review customer rating", ratingTask.getName());
        LOGGER.info("输出 ratingTask.getName = {}", ratingTask.getName());
        Task profitabilityTask = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
        assertEquals("Review profitability", profitabilityTask.getName());
        LOGGER.info("输出 profitabilityTask.getName = {}", profitabilityTask.getName());

        // Complete the management task by stating that not enough info was
        // provided
        // This should throw the error event, which closes the subprocess
        variables = new HashMap<>();
        variables.put("notEnoughInformation", true);
        taskService.complete(profitabilityTask.getId(), variables);

        // The 'provide additional details' task should now be active
        Task provideDetailsTask = taskService.createTaskQuery().taskAssignee("kermit").singleResult();
        assertEquals("Provide additional details", provideDetailsTask.getName());
        LOGGER.info("输出 provideDetailsTask.getName = {}", provideDetailsTask.getName());

        // Providing more details (ie. completing the task), will activate the
        // subprocess again
        taskService.complete(provideDetailsTask.getId());
        List<Task> reviewTasks = taskService.createTaskQuery().orderByTaskName().asc().list();
        assertEquals("Review customer rating", reviewTasks.get(0).getName());
        assertEquals("Review profitability", reviewTasks.get(1).getName());
        LOGGER.info("输出 reviewTasks.get(0).getName = {}", reviewTasks.get(0).getName());
        LOGGER.info("输出 reviewTasks.get(1).getName = {}", reviewTasks.get(1).getName());

        // Completing both tasks normally ends the process
        taskService.complete(reviewTasks.get(0).getId());
        variables.put("notEnoughInformation", false);
        taskService.complete(reviewTasks.get(1).getId(), variables);
        assertProcessEnded(processEngine, procId);

        Authentication.setAuthenticatedUserId(null);
    }
}
