package com.imooc.activiti.bpmn20;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试
 * 1. 在流程定义中设置候选用户和候选组，在taskService中claim assignee
 * 2. 在流程的TaskListener中设置UserTask候选用户、候选组、变量、过期时间等属性
 */
public class UserTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 在流程定义中设置UserTask的 候选用户和候选组，在taskService中claim assignee
     */
    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("user1").singleResult();
        LOGGER.info("通过 user1 查找 task ={}", task);task = taskService.createTaskQuery()
                .taskCandidateUser("user2").singleResult();
        LOGGER.info("通过 user2 查找 task ={}", task);
        task = taskService.createTaskQuery()
                .taskCandidateGroup("group1").singleResult();
        LOGGER.info("通过 group1 查找 task ={}", task);

        // 为Task设置Assignee用户，
        // 第一种会做校验，如果已设置则抛出异常，没有设置则会将指定用户设置为assignee，
        // 并使其他的候选用户和用户组失效
        taskService.claim(task.getId(), "user2");
        // 第二种不做校验，直接改变Assignee，不推荐使用
//        taskService.setAssignee(task.getId(), "user2");
        LOGGER.info("claim user2作为 task.id = {} 的Assignee", task.getId());
        task = taskService.createTaskQuery()
                .taskCandidateOrAssigned("user1").singleResult();
        LOGGER.info("通过 user1 查找 task ={}", task);
        task = taskService.createTaskQuery()
                .taskCandidateOrAssigned("user2").singleResult();
        LOGGER.info("通过 user2 查找 task ={}", task);
    }

    /**
     * 在流程的TaskListener中设置UserTask候选用户、候选组、变量、过期时间等属性
     */
    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery()
                .taskCandidateUser("user1").singleResult();
        LOGGER.info("通过 user1 查找 task ={}", task);task = taskService.createTaskQuery()
                .taskCandidateUser("user2").singleResult();
        LOGGER.info("通过 user2 查找 task ={}", task);
        task = taskService.createTaskQuery()
                .taskCandidateGroup("group1").singleResult();
        LOGGER.info("通过 group1 查找 task ={}", task);

        taskService.complete(task.getId());
    }
}
