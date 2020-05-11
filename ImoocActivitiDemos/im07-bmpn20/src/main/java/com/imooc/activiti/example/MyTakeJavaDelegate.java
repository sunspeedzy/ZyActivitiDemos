package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * my-process-subprocess1.bpmn20.xml中 确认收货 的 JavaDelegate类
 * 
 * @author zhangyan_g
 */
public class MyTakeJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTakeJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("运行我的 java delegate {}", this);

    }
}
