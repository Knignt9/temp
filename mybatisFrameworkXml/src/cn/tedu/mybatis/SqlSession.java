package cn.tedu.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.tedu.domain.Delete;
import cn.tedu.domain.Insert;
import cn.tedu.domain.Mapper;
import cn.tedu.domain.Select;
import cn.tedu.domain.TypeAlias;
import cn.tedu.domain.TypeAliases;
import cn.tedu.domain.Update;

public class SqlSession {
	private static Connection conn;
	private Mapper mapper;
	private TypeAliases typeAliases;
	private Object object;
	private static final Map<String,String> ResultType=new HashMap<String,String>();
	static{
		ResultType.put("map", "java.util.Map.HashMap");
		ResultType.put("hashmap", "java.util.Map.HashMap");
		ResultType.put("byte", "java.lang.Byte");
		ResultType.put("int", "java.lang.Integer");
		ResultType.put("char", "java.lang.Character");
		ResultType.put("double", "java.lang.Double");
		ResultType.put("float", "java.lang.Float");
		ResultType.put("float", "java.lang.Float");
	}
	public SqlSession(){}
	public SqlSession(Connection conn,Mapper mapper,TypeAliases typeAliases){
		this.conn=conn;
		this.mapper=mapper;
		this.typeAliases=typeAliases;
		open();
	}
	private Select chooseSelect(String args){
		return mapper.getSelectMap().get(args);
	}
	private Delete chooseDelete(String args){
		return mapper.getDeleteMap().get(args);
	}
	private Insert chooseInsert(String args){
		return mapper.getInsertMap().get(args);
	}
	private Update chooseUpdate(String args){
		return mapper.getUpdataMap().get(args);
	}
	public Connection getConnection(){
		return conn;
	}
	public void open(){
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void commit(){
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void rollback(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public <T>T selectOne(String arg0,Object arg1){
		Select select = chooseSelect(arg0);
		String parameterType = select.getParameterType();
		String result = select.getResultType();
		 String type = ResultType.get(result);
		 String sql=select.getText();
		 int rowAffect=0;
		if(ResultType.get(parameterType)!=null){
			if(sql.contains("#")){
				 String param=sql.split("#")[0];
//				 String key = sql.split("#")[1].split("[{]")[1].split("[}]")[0];
				 sql=param+arg1.toString();
			 }
		}
		if(type!=null){
			 
		}else{
			Class clazz = getClass(result);
			if(clazz!=null){
				System.out.println(sql);
				object=executeQuery(clazz,sql,null);
			}
		 }
		return (T) object;
		
	}
	public <T>T selectList(String args){
		Select select = chooseSelect(args);
		String result = select.getResultType();
		String sql = select.getText();
		List list=new ArrayList();
		if(ResultType.get(result)!=null){
			
		}else{
			Class clazz = getClass(result);
			if(clazz!=null){
				executeQuery(clazz,sql,list,null);
			}
		}
		return (T) list;
	}
	public <T>T selectList(String arg0,Object obj){
		Select select = chooseSelect(arg0);
		String result = select.getResultType();
		String sql = select.getText();
		String parameterType = select.getParameterType();
		List list=new ArrayList();
		if(ResultType.get(parameterType)!=null){
			
		}else{
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					String name = field.getName();
					Object objValue = field.get(obj);
					if(objValue!=null){
						String fieldValue = objValue.toString();
						if(sql.contains("like "+"#{"+name+"}")){
							fieldValue="'"+fieldValue+"'";
						}
						if(sql.contains("#{"+name+"}")){
							sql=sql.replace("#{"+name+"}", fieldValue);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		String returnType = ResultType.get(result);
		if(returnType!=null){
			Map<String,Object> map=new HashMap<>();
			list=executeQuery(map,sql,null);
		}else{
			System.out.println(sql);
			Class clazz = getClass(result);
			if(clazz!=null){
				executeQuery(clazz,sql,list,null);
				
			}
		}
		return (T) list;
	}
	public Class getClass(String result){
		Class clazz =null;
		for(TypeAlias typeAlias:typeAliases.getTypeAliase()){
			if(result.equals(typeAlias.getAlias())){
				String className = typeAlias.getType();
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return clazz;
	}
	public int delete(String arg0,Object arg1){
		Delete delete = chooseDelete(arg0);
		String sql = delete.getText();
		String parameterType = delete.getParameterType();
		int rowAffect=0;
		if(parameterType.equals("int")){
			sql=sql.split("#")[0]+arg1;
		}
		
		rowAffect=executeUpdate(sql,null);
		return rowAffect;
		
	}
	public int insert(String arg0,Object arg1){
		int rowAffect=0;
		Insert insert = chooseInsert(arg0);
		String parameterType = insert.getParameterType();
		String useGeneratedKeys = insert.getUseGeneratedKeys();
		String keyProperty = insert.getKeyProperty();
		String sql = insert.getText();
		if(ResultType.get(parameterType)!=null){
			
		}else{
			Field[] fields = arg1.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					Object fieldValue = field.get(arg1);
					String name = field.getName();
					System.out.println("name"+name+"    value:"+fieldValue);
					if(fieldValue!=null){
						if(field.getType().toString().contains("String")){
							fieldValue="'"+fieldValue+"'";
						}
						if(sql.contains("#{"+name+"}")){
							sql=sql.replace("#{"+name+"}", fieldValue.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if("true".equals(useGeneratedKeys)){
			sql=sql.replaceFirst("\\(", "\\("+keyProperty+",");
			StringBuilder sb=new StringBuilder(sql);
			sb.insert(sb.lastIndexOf("(")+1, "null,");
			sql=sb.toString();
		}
		System.out.println(sql);
		rowAffect=executeUpdate(sql,null);
		return rowAffect;
	}
	
	public int update(String arg0,Object arg1){
		int rowAffect=0;
		Update update = chooseUpdate(arg0);
		String parameterType = update.getParameterType();
		String sql = update.getText();
		if(parameterType.equalsIgnoreCase("hashmap")){
			Map map=(Map) arg1;
			Set key = map.keySet();
			for (Object object : key) {
				Object value = map.get(object);
				if(value.getClass().toString().contains("String")){
					value="'"+value+"'";
				}
				if(sql.contains("#{"+object.toString().trim()+"}")){
					sql=sql.replace("#{"+object.toString().trim()+"}", value.toString());
				}
			}
		}
		System.out.println(sql);
		
		rowAffect=executeUpdate(sql,null);
		return rowAffect;
	}
	
	
	
	public int executeUpdate(String sql,Object...args){
		PreparedStatement ps=null;
		int rowAffect =0;
		try {
			ps = conn.prepareStatement(sql);
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			rowAffect = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowAffect;
	}
	public <T>T executeQuery(Class clazz,String sql,Object...args){
		PreparedStatement ps=null;
		ResultSet rs=null;
		Object obj =null;
		try {
			ps=conn.prepareStatement(sql);
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			rs=ps.executeQuery();
			List<String> columnNames=new ArrayList<>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i+1);
				columnNames.add(columnName);
			}
			while(rs.next()){
				obj = clazz.newInstance();
				System.out.println(clazz.newInstance().getClass().getName());
				for (String columnName : columnNames) {
					String setMethodName="set"+columnName.substring(0, 1).toUpperCase()+columnName.substring(1);
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if(setMethodName.equals(method.getName())){
							System.out.println("11111111");
							Object value = rs.getObject(columnName);
							method.invoke(obj, value);
						}
					}
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) obj;
	}
	public <T>T executeQuery(Class clazz,String sql,List list,Object...args){
		PreparedStatement ps=null;
		ResultSet rs=null;
		Object obj =null;
		try {
			ps=conn.prepareStatement(sql);
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			rs=ps.executeQuery();
			List<String> columnNames=new ArrayList<>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i+1);
				columnNames.add(columnName);
			}
			while(rs.next()){
				obj = clazz.newInstance();
				for (String columnName : columnNames) {
					String setMethodName="set"+columnName.substring(0, 1).toUpperCase()+columnName.substring(1);
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if(setMethodName.equals(method.getName())){
							Object value = rs.getObject(columnName);
							method.invoke(obj, value);
						}
					}
				}
				list.add(obj);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) list;
	}
	public <T>T executeQuery(Map<String,Object> map,String sql,Object...args){
		PreparedStatement ps=null;
		ResultSet rs=null;
		System.out.println(sql);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			ps=conn.prepareStatement(sql);
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			rs=ps.executeQuery();
			List<String> columnNames=new ArrayList<>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i+1);
				columnNames.add(columnName);
			}
			while(rs.next()){
				for (String columnName : columnNames) {
					Object value = rs.getObject(columnName);
					map.put(columnName, value);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) list;
	}
	
	
	
	
	
	
	
	
	
	
	
}
