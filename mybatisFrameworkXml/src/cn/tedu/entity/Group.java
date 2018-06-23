package cn.tedu.entity;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String groupName;
	private String groupLoc;
	
	private List<User> users;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupLoc() {
		return groupLoc;
	}

	public void setGroupLoc(String groupLoc) {
		this.groupLoc = groupLoc;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupName=" + groupName + ", groupLoc=" + groupLoc + ", users=" + users + "]";
	}
	
	
	
	
	

}
