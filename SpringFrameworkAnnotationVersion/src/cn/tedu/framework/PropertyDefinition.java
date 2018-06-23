package cn.tedu.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyDefinition {
	private Properties prop=new Properties();
	public void initProp(String classpath){
		String path = PropertyDefinition.class.getClassLoader().getResource(classpath).getPath();
		try {
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getProp(String key){
		return prop.getProperty(key);
	}
}
