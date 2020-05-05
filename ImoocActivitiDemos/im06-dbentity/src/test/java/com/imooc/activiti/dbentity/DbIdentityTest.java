package com.imooc.activiti.dbentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示内容：
 * 向 ACT_ID_* 表写入数据
 * 
 * @author zhangyan
 *
 */
public class DbIdentityTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbIdentityTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql-idhis.cfg.xml");
	
	@Test
	public void testIdentity() {
		LOGGER.info("对用户和用户组进行操作");
		IdentityService identityService = activitiRule.getIdentityService();
		// 创建User，向 ACT_ID_USER 表中写数据
		User user1 = identityService.newUser("user1");
		user1.setFirstName("firstName");
		user1.setLastName("lastName");
		user1.setEmail("user1@126.com");
		user1.setPassword("pwd");
		identityService.saveUser(user1);
		
		User user2 = identityService.newUser("user2");
		identityService.saveUser(user2);
		// 创建Group，向 ACT_ID_GROUP 表中写数据
		Group group1 = identityService.newGroup("group1");
		group1.setName("for test");
		identityService.saveGroup(group1);
		// 向 ACT_ID_MEMBERSHIP 表中写数据
		identityService.createMembership(user1.getId(), group1.getId());
		identityService.createMembership(user2.getId(), group1.getId());
		
		// 向 ACT_ID_INFO 写数据
		identityService.setUserInfo(user1.getId(), "age", "18");
		identityService.setUserInfo(user1.getId(), "address", "Beijing");
	}
	
}
