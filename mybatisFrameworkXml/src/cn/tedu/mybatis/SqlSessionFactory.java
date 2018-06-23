package cn.tedu.mybatis;

import java.net.URL;
import java.sql.Connection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.tedu.domain.Delete;
import cn.tedu.domain.Insert;
import cn.tedu.domain.Mapper;
import cn.tedu.domain.Select;
import cn.tedu.domain.TypeAliases;
import cn.tedu.domain.Update;

public class SqlSessionFactory {
	private static Connection conn;
	private String resource;
	private  TypeAliases typeAliases;
	private static Mapper mapper=new Mapper();
	public SqlSessionFactory(Connection conn,String resource,TypeAliases typeAliases){
		this.conn=conn;
		this.resource=resource;
		this.typeAliases=typeAliases;
		readXml();
	}
	
	public SqlSessionFactory(){
		
	}
	private void readXml(){
		SAXReader saxReader=new SAXReader();
		URL url = this.getClass().getClassLoader().getResource(resource);
		mapper.setResource(resource);
		try {
			Document document = saxReader.read(url);
			Element root = document.getRootElement();
			List<Element> elements = root.elements("select");
			for (Element select : elements) {
				Select s=new Select();
				String id = select.attributeValue("id");
				String resultType = select.attributeValue("resultType");
				String resultMap = select.attributeValue("resultMap");
				String paramType = select.attributeValue("parameterType");
				String paramMap = select.attributeValue("parameterMap");
				String text = select.getText();
				s.setId(id);
				s.setText(text);
				s.setParameterMap(paramMap);
				s.setParameterType(paramType);
				s.setResultMap(resultMap);
				s.setResultType(resultType);
				mapper.getSelectMap().put(id, s);
			}
			
			List<Element> deleteElements = root.elements("delete");
			for (Element deleteElement : deleteElements) {
				Delete delete=new Delete();
				String id = deleteElement.attributeValue("id");
				String parameterType = deleteElement.attributeValue("parameterType");
				String parameterMap = deleteElement.attributeValue("parameterMap");
				String text = deleteElement.getText();
				delete.setId(id);
				delete.setText(text);
				delete.setParameterMap(parameterMap);
				delete.setParameterType(parameterType);
				mapper.getDeleteMap().put(id, delete);
			}
			
			List<Element> insertElements = root.elements("insert");
			for (Element insertElement : insertElements) {
				Insert insert=new Insert();
				String id = insertElement.attributeValue("id");
				String parameterType = insertElement.attributeValue("parameterType");
				String parameterMap = insertElement.attributeValue("parameterMap");
				String keyProperty = insertElement.attributeValue("keyProperty");
				String useGeneratedKeys = insertElement.attributeValue("useGeneratedKeys");
				String text = insertElement.getText();
				insert.setId(id);
				insert.setText(text);
				insert.setParameterType(parameterType);
				insert.setKeyProperty(keyProperty);
				insert.setParameterMap(parameterMap);
				insert.setUseGeneratedKeys(useGeneratedKeys);
				mapper.getInsertMap().put(id, insert);
			}
			
			List<Element> updateElements = root.elements();
			for (Element updateElement : updateElements) {
				Update update=new Update();
				String id = updateElement.attributeValue("id");
				String text = updateElement.getText();
				String parameterType = updateElement.attributeValue("parameterType");
				String parameterMap = updateElement.attributeValue("parameterMap");
				update.setId(id);
				update.setParameterType(parameterType);
				update.setText(text);
				update.setParameterMap(parameterMap);
				mapper.getUpdataMap().put(id, update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public  SqlSession openSession(){
		return new SqlSession(conn,mapper,typeAliases);
	}
	
	
	
	
	
	
	
}
