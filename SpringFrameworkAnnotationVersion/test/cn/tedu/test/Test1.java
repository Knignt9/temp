package cn.tedu.test;

import org.junit.Test;

import cn.tedu.bean.JDBCUtil;
import cn.tedu.framework.SpringAnnotationFramework;
import cn.tedu.service.UserService;
import cn.tedu.service.impl.UserServiceImpl;

public class Test1 {
	@Test
	public void test(){
		SpringAnnotationFramework context=new SpringAnnotationFramework("resources/framework.xml");
		UserService userDao = context.getBean("userService",UserService.class);
		userDao.addUser();
//		JDBCUtil jdbc = context.getBean("jdbcUtil",JDBCUtil.class);
//		System.out.println(jdbc.getUserPassword());
	}
	
}
