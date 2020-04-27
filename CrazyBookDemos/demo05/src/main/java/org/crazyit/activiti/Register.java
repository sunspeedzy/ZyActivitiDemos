package org.crazyit.activiti;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

/**
 * 注册与注销ProcessEngine实例
 * @author yangenxiong
 *
 */
public class Register {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//读取自定义配置
		ProcessEngineConfiguration config = ProcessEngineConfiguration.
				createProcessEngineConfigurationFromResource("register.xml");
		//创建ProcessEngine实例
		// buildProcessEngine 方法会创建 ProcessEngine实例，而ProcessEngine的构造函数中包含了向ProcessEngines注册自己的实现
		ProcessEngine engine = config.buildProcessEngine();
		//获取ProcessEngine的Map
		Map<String, ProcessEngine> engines = ProcessEngines.getProcessEngines();
		System.out.println("注册后引擎数：" + engines.size());
		//注销ProcessEngine实例
		// 只是将ProcessEngine实例从ProcessEngines的Map中删除，并不会调用这个ProcessEngine实例的close方法
		ProcessEngines.unregister( engine);
		System.out.println("调用unregister后引擎数：" + engines.size());
	}

}
