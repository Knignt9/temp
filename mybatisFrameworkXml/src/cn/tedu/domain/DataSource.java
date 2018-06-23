package cn.tedu.domain;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
	private String type;
	private List<Property> property=new ArrayList<>();
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Property> getProperty() {
		return property;
	}
	public void setProperty(List<Property> property) {
		this.property = property;
	}
	
}
