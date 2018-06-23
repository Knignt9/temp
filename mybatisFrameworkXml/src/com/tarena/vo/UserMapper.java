package com.tarena.vo;

import java.util.List;
import java.util.Map;

import cn.tedu.entity.Group;
import cn.tedu.entity.User;

public interface UserMapper {
	public User findUserById(Integer id);
	public List<User> findUserByPage(Page page);
	
	public int addUser(User user);
	public int deleteUser(Integer id);
	public int updateUser(Map user);
	
	public List<User> findUserByIf(Map data);
	public List<User> findUserByChoose(User user);
	public List<User> findUserByWhere(Map data);
	public List<User> findUserByTrim1(Map data);
	public int updateUserBySet(User user);
	public int updateUserByTrim2(User user);
	
	public List<User> findUserByIds(List list);
	
	public List<User> findUser_ForOneGroup();
	public List<Group> findGroup_ForManyUser();
	
	
	
}
