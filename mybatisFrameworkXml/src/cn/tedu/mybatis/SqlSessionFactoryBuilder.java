package cn.tedu.mybatis;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.tedu.domain.DataSource;
import cn.tedu.domain.Mapper;
import cn.tedu.domain.Properties;
import cn.tedu.domain.Property;
import cn.tedu.domain.TypeAlias;
import cn.tedu.domain.TypeAliases;


public class SqlSessionFactoryBuilder {
	private static Properties props=new Properties();
	private Mapper map=new Mapper();
	private TypeAliases typeAliases=new TypeAliases();
	private static DataSource datas=new DataSource();
	private static java.util.Properties prop=new java.util.Properties(); 
	private static JDBC jdbc=new JDBC();
	private static Connection conn;
	private void initProperty(){
		String resource = props.getResource();
		System.out.println(resource);
		String path = this.getClass().getClassLoader().getResource(resource).getPath();
		
		try {
			prop.load(new FileInputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void injectJDBC(){
		for(Property p:datas.getProperty()){
			String name = p.getName();
			String value = p.getValue();
			if(value.contains("$")){
				value=value.split("[{]")[1].split("[}]")[0];
			}
			value=prop.getProperty(value);
			String setMethodName="set"+name.substring(0, 1).toUpperCase()+name.substring(1);
			Method[] methods = jdbc.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if(setMethodName.equals(method.getName())){
					try {
						method.invoke(jdbc, value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	private void initConnection(){
		try {
			Class.forName(jdbc.getDriver());
			conn=DriverManager.getConnection(jdbc.getUrl(),jdbc.getUser(),jdbc.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public SqlSessionFactory built(InputStream in){
		SAXReader saxReader=new SAXReader();
		try {
			Document document = saxReader.read(in);
			Element root = document.getRootElement();
			Element confElement = root.element("properties");
			String resource = confElement.attributeValue("resource");
			props.setResource(resource);
			
			Element typeElement = root.element("typeAliases");
			List<Element> typeElements = typeElement.elements();
			for (Element element : typeElements) {
				TypeAlias typeAlias=new TypeAlias();
				String type = element.attributeValue("type");
				String alias = element.attributeValue("alias");
				typeAlias.setAlias(alias);
				typeAlias.setType(type);
				typeAliases.getTypeAliase().add(typeAlias);
			}
			
			Element mapper = root.element("mappers").element("mapper");
			String path = mapper.attributeValue("resource");
			map.setResource(path);
			
			Element envirElement = root.element("environments");
			String data = envirElement.attributeValue("default");
			
			List<Element> elements = envirElement.elements();
			for (Element ele : elements) {
				String id = ele.attributeValue("id");
				Element datatype = ele.element("transactionManager");
				
				Element dataSource = ele.element("dataSource");
				String type = dataSource.attributeValue("type");
				
				List<Element> propsElements = dataSource.elements();
				for (Element element : propsElements) {
					String name = element.attributeValue("name");
					String value = element.attributeValue("value");
					Property property=new Property();
					property.setName(name);
					property.setValue(value);
					datas.getProperty().add(property);
				}
			}
			initProperty();
			injectJDBC();
			initConnection();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return new SqlSessionFactory(conn,map.getResource(),typeAliases);
	}
}
