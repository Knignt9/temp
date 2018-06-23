package cn.tedu.domain;

import java.util.HashMap;
import java.util.Map;

public class Mapper {
	private String resource;
	private String namespace;
	private Map<String,Select> selectMap=new HashMap<>();
	private Map<String,Delete> deleteMap=new HashMap<>();
	private Map<String,Update> updataMap=new HashMap<>();
	private Map<String,Insert> insertMap=new HashMap<>();
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public Map<String, Insert> getInsertMap() {
		return insertMap;
	}
	public void setInsertMap(Map<String, Insert> insertMap) {
		this.insertMap = insertMap;
	}
	public Map<String, Delete> getDeleteMap() {
		return deleteMap;
	}
	public void setDeleteMap(Map<String, Delete> deleteMap) {
		this.deleteMap = deleteMap;
	}
	public Map<String, Update> getUpdataMap() {
		return updataMap;
	}
	public void setUpdataMap(Map<String, Update> updataMap) {
		this.updataMap = updataMap;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public Map<String, Select> getSelectMap() {
		return selectMap;
	}
	public void setSelectMap(Map<String, Select> selectMap) {
		this.selectMap = selectMap;
	}
	
	
	
}
