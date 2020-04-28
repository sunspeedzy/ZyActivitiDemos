package com.imooc.activiti.interceptor;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

/**
 * MDC Test
 * 
 * @author zhangyan_g
 *
 */
public class MDCCommandInvoker extends DebugCommandInvoker {

	@Override
	public void executeOperation(Runnable runnable) {
		boolean mdcEnabled = LogMDC.isMDCEnabled();
		// 开启MDC
		LogMDC.setMDCEnabled(true);
		if (runnable instanceof AbstractOperation) {
			AbstractOperation operation = (AbstractOperation) runnable;

			if (operation.getExecution() != null) {
				// 将 可操作对象 放入MDC中
				LogMDC.putMDCExecution(operation.getExecution());
			}

		}
		super.executeOperation(runnable);
		LogMDC.clear();
		if (!mdcEnabled) {
			LogMDC.setMDCEnabled(mdcEnabled);
		}
	}

}
