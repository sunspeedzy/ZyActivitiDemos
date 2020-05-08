package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 自制JavaDelegate类
 * @author zhangyan_g
 */
public class MyJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);
    /**
     * 用于属性注入
     */
    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {
        if (name != null) {
            Object value = name.getValue(execution);
            LOGGER.info("属性 name = {}", value);
        }
        if (desc != null) {
            Object value = desc.getValue(execution);
            LOGGER.info("属性 desc = {}", value);
        }
        LOGGER.info("运行我的 java delegate {}", this);

    }
}
