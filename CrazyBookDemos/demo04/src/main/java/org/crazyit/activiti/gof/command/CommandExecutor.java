package org.crazyit.activiti.gof.command;

/**
 * 命令执行者
 * @author yangenxiong
 *
 */
public class CommandExecutor {

	public void execute(Command command) {
		//创建命令接收者可以使用其他设计模式
		command.execute(new CommandReceiverImpl());
	}
}
