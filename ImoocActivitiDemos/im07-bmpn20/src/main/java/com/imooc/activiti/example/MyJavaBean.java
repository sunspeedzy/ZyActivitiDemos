package com.imooc.activiti.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 测试 ServiceTask中配置 执行JavaBean的调用方法表达式和值表达式
 * @author zhangyan_g
 */
public class MyJavaBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaBean.class);

    private String name;

    public String getName() {
        LOGGER.info("运行 getName 方法：{}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyJavaBean() {
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    public void sayHello() {
        LOGGER.info("运行 sayHello 方法");
    }
}
