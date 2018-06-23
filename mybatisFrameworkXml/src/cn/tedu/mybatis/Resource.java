package cn.tedu.mybatis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resource {
	public static InputStream getResourceAsStream(String resource){
		InputStream in=null;
		try {
			String path = Resource.class.getClassLoader().getResource(resource).getPath();
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return in;
	}
}
