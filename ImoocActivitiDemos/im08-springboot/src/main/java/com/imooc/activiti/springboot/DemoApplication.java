package com.imooc.activiti.springboot;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author zhangyan_g
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DemoApplication {
    private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Connector testConnector() {
        return integrationContext -> {
            logger.info("以前叫代理，现在叫连接器被调用啦~~");
            return integrationContext;
        };
    }
}
