package org.crazyit.activiti.gof.command.impl;

import org.crazyit.activiti.gof.command.Command;
import org.crazyit.activiti.gof.command.CommandReceiver;

/**
 * 命令实现B
 * @author yangenxiong
 *
 */
public class CommandB implements Command {

	@Override
	public void execute(CommandReceiver receiver) {
		receiver.doSomethingB();
	}

}
