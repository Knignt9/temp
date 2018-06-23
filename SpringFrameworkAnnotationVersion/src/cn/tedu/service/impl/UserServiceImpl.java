package cn.tedu.service.impl;


import cn.tedu.annotation.Compnent;
import cn.tedu.annotation.Resource;
import cn.tedu.dao.UserDao;
import cn.tedu.service.UserService;
@Compnent(value="userService")
public class UserServiceImpl implements UserService {
	@Resource(name="userDao")
	private UserDao userdao;
	
	
	public void setUserDao(UserDao userdao) {
		this.userdao = userdao;
	}

	@Override
	public void addUser() {
		System.out.println("UserServiceImpl.addUser()");
		userdao.addUser();
	}

}
