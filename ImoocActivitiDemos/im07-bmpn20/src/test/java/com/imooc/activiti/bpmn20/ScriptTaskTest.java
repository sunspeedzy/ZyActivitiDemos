package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 测试
 * 1. Grooy 脚本任务 设置变量
 * 2. Juel 脚本任务 设置返回值
 * 3. JavaScript 脚本任务 设置返回值
 * 4. 脚本内容测试方法
 */
public class ScriptTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * Grooy 脚本任务 设置变量
     */
    @Test
    @Deployment(resources = {"my-process-scripttask1.bpmn20.xml"})
    public void testScriptTask1() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .orderByVariableName().asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("变量 = {}", historicVariableInstance);
        }
        LOGGER.info("变量数量 = {}", historicVariableInstances.size());
    }

    /**
     * Juel 脚本任务 设置返回值
     */
    @Test
    @Deployment(resources = {"my-process-scripttask2.bpmn20.xml"})
    public void testScriptTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .orderByVariableName().asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("变量 = {}", historicVariableInstance);
        }
        LOGGER.info("变量数量 = {}", historicVariableInstances.size());
    }

    /**
     * JavaScript 脚本任务 设置返回值
     */
    @Test
    @Deployment(resources = {"my-process-scripttask3.bpmn20.xml"})
    public void testScriptTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .orderByVariableName().asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("变量 = {}", historicVariableInstance);
        }
        LOGGER.info("变量数量 = {}", historicVariableInstances.size());
    }
    /**
     * 脚本内容测试方法
     */
    @Test
    public void testScriptEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");
        Object eval = scriptEngine.eval("${1 + 2}");

        // 整数相加结果为 Long，小数相加结果为 Double
        LOGGER.info("${1 + 2} 结果 = {}", eval);
        assertTrue(eval instanceof Long);

        eval = scriptEngine.eval("${1.0 + 2}");
        LOGGER.info("${1.0 + 2} 结果 = {}", eval);
        assertTrue(eval instanceof Double);
    }
}
