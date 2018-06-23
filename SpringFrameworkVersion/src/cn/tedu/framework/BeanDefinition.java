package cn.tedu.framework;

import java.util.HashMap;
import java.util.Map;

import cn.tedu.framework.ConstructorArgDefinition;

public class BeanDefinition {
	private String id;
	private String className;
	private Map<String,ConstructorArgDefinition> consts=new HashMap<String,ConstructorArgDefinition>();
	private Map<String,PropertyDefiniton> beanMap=new HashMap<String,PropertyDefiniton>();
	
	public Map<String, ConstructorArgDefinition> getConsts() {
		return consts;
	}
	public void setConsts(Map<String, ConstructorArgDefinition> consts) {
		this.consts = consts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Map<String, PropertyDefiniton> getBeanMap() {
		return beanMap;
	}
	public void setBeanMap(Map<String, PropertyDefiniton> beanMap) {
		this.beanMap = beanMap;
	}
	
}
