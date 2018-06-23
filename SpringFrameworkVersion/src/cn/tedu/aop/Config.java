package cn.tedu.aop;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private String proxy_target_class;
	private Map<String,Aspect> aspectMap=new HashMap<String,Aspect>();
	public String getProxy_target_class() {
		return proxy_target_class;
	}
	public void setProxy_target_class(String proxy_target_class) {
		this.proxy_target_class = proxy_target_class;
	}
	public Map<String, Aspect> getAspectMap() {
		return aspectMap;
	}
	public void setAspectMap(Map<String, Aspect> aspectMap) {
		this.aspectMap = aspectMap;
	}
	
	
}
