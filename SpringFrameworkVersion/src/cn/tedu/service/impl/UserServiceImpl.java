package cn.tedu.service.impl;

import cn.tedu.dao.UserDao;
import cn.tedu.service.UserService;

public class UserServiceImpl implements UserService {
	private UserDao userdao;
	
	public void setUserDao(UserDao userdao) {
		this.userdao = userdao;
	}

	@Override
	public void addUser() {
		System.out.println("UserServiceImpl.addUser()");
//		userdao.addUser();
	}

}
