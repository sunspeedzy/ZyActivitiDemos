package org.crazyit.activiti.gof.chain.impl;

import org.crazyit.activiti.gof.chain.Handler;
import org.crazyit.activiti.gof.chain.Request;

public class HandlerB extends Handler {

	public void execute(Request request) {
		//处理自己的事，然后交由一下任处理者执行请求
		System.out.println("请求处理者B处理请求");
		next.execute(request);
	}

}
