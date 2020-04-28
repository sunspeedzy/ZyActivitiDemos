package com.imooc.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来演示在执行 com.imooc.activiti.MyUnitTest 时打印MDC的效果
 * 
 * @author zhangyan_g
 *
 */
public class MDCErrorDelegate implements JavaDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(MDCErrorDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {
		// TODO Auto-generated method stub
		LOGGER.info("run MDCErrorDelegate");
		throw new RuntimeException("only test MDC");
	}

}
