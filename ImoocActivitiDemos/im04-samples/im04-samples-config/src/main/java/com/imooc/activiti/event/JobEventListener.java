package com.imooc.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义事件监听器
 * 
 * @author zhangyan_g
 *
 */
public class JobEventListener implements ActivitiEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobEventListener.class);

	@Override
	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType= event.getType();
		String name = eventType.name();
		
		if (name.startsWith("TIMER") || name.startsWith("JOB")) {
			LOGGER.info("监听到Job事件 {} \t {}", eventType, event.getProcessInstanceId());
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

}
