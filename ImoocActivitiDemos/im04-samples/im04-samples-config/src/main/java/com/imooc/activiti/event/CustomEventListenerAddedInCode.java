package com.imooc.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义事件监听器，以代码的方式添加到RuntimeService
 * 
 * @author zhangyan_g
 *
 */
public class CustomEventListenerAddedInCode implements ActivitiEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventListenerAddedInCode.class);

	@Override
	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType= event.getType();
		if (ActivitiEventType.CUSTOM.equals(eventType)) {
			LOGGER.info("自定义监听器以代码的方式添加到RuntimeService中，监听到自定义事件 {} \t {}",
					eventType, event.getProcessInstanceId());
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

}
