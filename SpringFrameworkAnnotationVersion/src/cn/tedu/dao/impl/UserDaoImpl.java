package cn.tedu.dao.impl;

import cn.tedu.annotation.Compnent;
import cn.tedu.dao.UserDao;
@Compnent(value="userDao")
public class UserDaoImpl implements UserDao {

	@Override
	public void addUser() {
		System.out.println("UserDaoImpl.add()");
	}

}
