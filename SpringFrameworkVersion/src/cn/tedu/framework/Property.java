package cn.tedu.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Property {
	private static Properties prop=new Properties();
	public static void initProp(String classpath){
		String path = Property.class.getClassLoader().getResource(classpath).getPath();
		try {
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Properties getProp(){
		return prop;
	}
}
