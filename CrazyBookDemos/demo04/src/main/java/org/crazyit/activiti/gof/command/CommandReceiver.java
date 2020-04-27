package org.crazyit.activiti.gof.command;

/**
 * 命令接者接口
 * @author yangenxiong
 *
 */
public interface CommandReceiver {

	//命令执行者方法A
	void doSomethingA();

	//命令执行者方法B
	void doSomethingB();
}
