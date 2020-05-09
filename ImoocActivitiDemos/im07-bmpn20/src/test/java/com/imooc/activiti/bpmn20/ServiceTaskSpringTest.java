package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import com.imooc.activiti.example.MyJavaBean;
import com.imooc.activiti.example.MyJavaDelegate;
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
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * 测试内容
 * 1.执行调用方法表达式和值表达式
 * 2.显示地传入 MyJavaDelegate实例 替换Spring的Bean配置
 * 3.ServiceTask中执行调用方法表达式和值表达式
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-context.xml")
public class ServiceTaskSpringTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Resource
    @Rule
    public ActivitiRule activitiRule;

    /**
     * 测试 自制JavaDelegate实现类提供的Service
     */
    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask41() {
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
     * 测试 显示地传入 MyJavaDelegate实例 替换Spring的Bean配置
     */
    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask42() {
        // 将 MyJavaDelegate 实例作为参数值，显示地传给流程，
        // 通过这种方式替换SpringContext中配置的单例bean
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        LOGGER.info("创建的myJavaDelegate = {}", myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        List<HistoricActivityInstance> activityInstances =
                activitiRule.getHistoryService()
                        .createHistoricActivityInstanceQuery()
                        .orderByHistoricActivityInstanceId().asc()
                        .listPage(0, 100);
        for (HistoricActivityInstance activityInstance : activityInstances) {
            LOGGER.info("执行的Activiti = {}", activityInstance);
        }
    }

    /**
     * 测试 ServiceTask中执行调用方法表达式和值表达式
     */
    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask5() {
        // 将 MyJavaBean 实例作为参数值，显示地传给流程，
        // 通过这种方式可以无需在 activiti-context.xml 中配置bean
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean("测试");

        LOGGER.info("创建的 myJavaBean = {}", myJavaBean);
        variables.put("myJavaBean", myJavaBean);

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        List<HistoricActivityInstance> activityInstances =
                activitiRule.getHistoryService()
                        .createHistoricActivityInstanceQuery()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .listPage(0, 100);
        for (HistoricActivityInstance activityInstance : activityInstances) {
            LOGGER.info("执行的Activiti = {}", activityInstance);
        }
    }
}
