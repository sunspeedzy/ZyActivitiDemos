package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 自制ActivitiBehavior类
 * @author zhangyan_g
 */
public class MyActivitiBehavior implements ActivityBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyActivitiBehavior.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("运行我的 ActivityBehavior {}", this);
        
    }
}
