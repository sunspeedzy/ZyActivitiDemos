package com.imooc.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程 Event 监听器
 * 
 * @author zhangyan_g
 *
 */
public class ProcessEventListener implements ActivitiEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

	@Override
	public void onEvent(ActivitiEvent event) {
		// TODO Auto-generated method stub
		ActivitiEventType eventType= event.getType();
		if (ActivitiEventType.PROCESS_STARTED.equals(eventType)) {
			LOGGER.info("流程启动 {} \t {}", eventType, event.getProcessInstanceId());
		} else if (ActivitiEventType.PROCESS_COMPLETED.equals(eventType)) {
			LOGGER.info("流程结束 {} \t {}", eventType, event.getProcessInstanceId());
		}
	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
