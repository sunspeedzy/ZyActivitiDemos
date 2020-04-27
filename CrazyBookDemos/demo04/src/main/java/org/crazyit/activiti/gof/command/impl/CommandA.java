package org.crazyit.activiti.gof.command.impl;

import org.crazyit.activiti.gof.command.Command;
import org.crazyit.activiti.gof.command.CommandReceiver;

/**
 * 命令实现A
 * @author yangenxiong
 *
 */
public class CommandA implements Command {
	public void execute(CommandReceiver receiver) {
		receiver.doSomethingA();
	}
}
