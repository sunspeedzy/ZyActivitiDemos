package com.imooc.activiti.coreapi;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试 IdentityService
 * 
 * @author zhangyan_g
 *
 */
public class IdentityServiceTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(IdentityServiceTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void testIdentityService() {
		LOGGER.info("测试Identity服务");
		IdentityService identityService = activitiRule.getIdentityService();
		// 创建user
		User user1 = identityService.newUser("user1");
		user1.setEmail("user1@126.com");
		User user2 = identityService.newUser("user2");
		user2.setEmail("user2@126.com");
		// 向数据库保存user
		identityService.saveUser(user1);
		identityService.saveUser(user2);
		// 创建group
		Group group1 = identityService.newGroup("group1");
		// 向数据库保存group
		identityService.saveGroup(group1);
		Group group2 = identityService.newGroup("group2");
		identityService.saveGroup(group2);
		// 创建user和group的关系
		identityService.createMembership("user1", "group1");
		identityService.createMembership("user2", "group1");
		identityService.createMembership("user1", "group2");
		// 修改已写入数据库的user1的属性
		User user11 = identityService.createUserQuery().userId("user1").singleResult();
		user11.setLastName("Rocky");
		identityService.saveUser(user11);
		
		// 获取属于group1的user
		List<User> userList = identityService.createUserQuery().memberOfGroup("group1").listPage(0, 100);
		for (User user : userList) {
			LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
		}
		// 获取user1所属的group
		List<Group> groupList = identityService.createGroupQuery().groupMember("user1").listPage(0, 100);
		for (Group group : groupList) {
			LOGGER.info("group = {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
		}
	}
	
}
