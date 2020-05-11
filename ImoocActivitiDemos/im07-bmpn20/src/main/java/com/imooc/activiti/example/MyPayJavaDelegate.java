package com.imooc.activiti.example;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * my-process-subprocess1.bpmn20.xml中 确认支付 的 JavaDelegate类
 *
 * @author zhangyan_g
 */
public class MyPayJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPayJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("查看传入子流程的变量 {}", execution.getVariables());
        LOGGER.info("运行我的 java delegate {}", this);
        // 设置流程中的 Local 变量
        execution.getParent().setVariableLocal("key2", "value2");
        execution.getParent().setVariable("parentVariable", "parentValue");
        execution.setVariable("variable", "value");
        execution.setVariableLocal("localVariable", "localValue");
        // 设置返回的变量
        execution.setVariable("key1", "value1_1");
        execution.setVariable("key3", "value3");
        // 抛出 bpmnError，以测试 边界事件
        Object errorflag = execution.getVariable("errorflag");
        if (Objects.equals(errorflag, true)) {
            // 参数值要与 流程定义文件 > errorEventDefinition > errorRef 一致
            throw new BpmnError("bpmnError");
        }
    }
}
