package com.imooc.activiti.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计执行时间
 * 
 * @author zhangyan_g
 *
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(DurationCommandInterceptor.class);

	@Override
	public <T> T execute(CommandConfig config, Command<T> command) {
		long start = System.currentTimeMillis();
		try {
			return this.getNext().execute(config, command);
		} finally {
			long duration = System.currentTimeMillis() - start;
			LOGGER.info("{} 执行时长 {} 毫秒",
					command.getClass().getSimpleName(), duration);
		}
		
	}

}
