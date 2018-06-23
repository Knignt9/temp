package test;

import cn.tedu.bean.Jdbc;
import cn.tedu.framework.SpringModule;
import cn.tedu.service.UserService;
import cn.tedu.service.impl.UserServiceImpl;
import constructor.User;

public class Test {
	public static void main(String[] args) throws Exception {
		SpringModule context=new SpringModule();
		User user = context.getBean("user",User.class);
		UserService userService = context.getBean("service", UserService.class);
		System.out.println(user.getAge());
		System.out.println(user.getUsername());
		userService.addUser();
		
		System.out.println("---------------");
		Jdbc jdbc=(Jdbc) context.getBean("jdbcObj");
		System.out.println(jdbc.getClassName());
//		System.out.println(Class.forName("com.tarena.service"));
//		System.out.println(new Test().getClass());
	}
}
